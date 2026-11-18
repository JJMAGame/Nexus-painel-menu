package com.nexusmaven;

import static com.nexusmaven.ApoioDeTestes.capturarSaida;
import static com.nexusmaven.ApoioDeTestes.entrada;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Fluxo de Locações de ponta a ponta: teclado simulado, saída capturada e
 * bancos JSON temporários (não toca nos arquivos reais do projeto).
 */
public class MenuLocacoesTest {

    private static final String CPF_INQUILINO = "12312312399";
    private static final String IMOVEL = "Casa do Pedro";

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    private String usuarios;
    private String imoveis;
    private String locacoes;

    @Before
    public void prepararBancos() {
        usuarios = new File(pasta.getRoot(), "usuarios.json").getPath();
        imoveis = new File(pasta.getRoot(), "imoveis.json").getPath();
        locacoes = new File(pasta.getRoot(), "locacoes.json").getPath();

        JsonUtil.salvar(usuarios, List.of(
                new admin("Chefe", "admin@nexus.com", "admin123", "99999999900"),
                new inquilino("João", "joao@nexus.com", "comum123", CPF_INQUILINO)));
        // Aluguel 1000 -> a regra de renda exige pelo menos 3000.
        JsonUtil.salvar(imoveis, List.of(
                new Imovel(IMOVEL, "Rua A, 10", "88888888888", 1000.0)));
    }

    // ---------- helpers ----------

    private String rodar(String... digitado) {
        return capturarSaida(() -> new MenuLocacoes(entrada(digitado), usuarios, imoveis, locacoes).abrir());
    }

    private List<ProcessoLocacao> processos() {
        return new LocacaoService(locacoes).listar();
    }

    private void salvarProcesso(EtapaLocacao etapa, double renda, String... pendencias) {
        ProcessoLocacao p = new ProcessoLocacao("LOC-001", CPF_INQUILINO, IMOVEL, etapa, renda);
        for (String pendencia : pendencias) {
            p.adicionarPendencia(pendencia);
        }
        new LocacaoService(locacoes).cadastrar(p);
    }

    // ---------- proposta ----------

    @Test
    public void novaPropostaCriaProcessoNaEtapaPropostaComPendenciaInicial() {
        String saida = rodar("1", "1", "1", "5000", "0");

        List<ProcessoLocacao> lista = processos();
        assertEquals(1, lista.size());
        ProcessoLocacao p = lista.get(0);
        assertEquals("LOC-001", p.getId());
        assertEquals(EtapaLocacao.PROPOSTA, p.getEtapaAtual());
        assertTrue(p.getPendencias().contains("Aguardando análise da proposta"));
        assertTrue(saida, saida.contains("Proposta LOC-001 criada"));
    }

    // ---------- avançar / voltar ----------

    @Test
    public void naoAvancaComPendenciaAberta() {
        salvarProcesso(EtapaLocacao.PROPOSTA, 5000.0, "Aguardando análise da proposta");

        String saida = rodar("4", "1", "1", "0");

        assertTrue(saida, saida.contains("Resolva as pendências"));
        assertEquals(EtapaLocacao.PROPOSTA, processos().get(0).getEtapaAtual());
    }

    @Test
    public void avancaDepoisDeResolverPendenciaEGeraNovaPendencia() {
        salvarProcesso(EtapaLocacao.PROPOSTA, 5000.0, "Aguardando análise da proposta");

        rodar("5", "1", "1", "0");                 // resolve a pendência
        String saida = rodar("4", "1", "1", "0");  // avança

        ProcessoLocacao p = processos().get(0);
        assertEquals(EtapaLocacao.DOCUMENTOS, p.getEtapaAtual());
        assertTrue(p.getPendencias().contains("Entregar RG, CPF e comprovante de renda"));
        assertTrue(saida, saida.contains("movido para: Documentos"));
    }

    @Test
    public void voltaUmaEtapa() {
        salvarProcesso(EtapaLocacao.CONTRATO, 5000.0);

        String saida = rodar("4", "1", "2", "0");

        assertEquals(EtapaLocacao.VISTORIA, processos().get(0).getEtapaAtual());
        assertTrue(saida, saida.contains("movido para: Vistoria"));
    }

    // ---------- regra de renda (3x o aluguel) ----------

    @Test
    public void bloqueiaRendaInsuficienteNaAnaliseDeRenda() {
        salvarProcesso(EtapaLocacao.ANALISE_RENDA, 2000.0); // 2000 < 3x1000

        String saida = rodar("4", "1", "1", "0");

        assertTrue(saida, saida.contains("Renda insuficiente"));
        assertEquals(EtapaLocacao.ANALISE_RENDA, processos().get(0).getEtapaAtual());
    }

    @Test
    public void liberaRendaSuficienteNaAnaliseDeRenda() {
        salvarProcesso(EtapaLocacao.ANALISE_RENDA, 3000.0); // 3000 == 3x1000

        String saida = rodar("4", "1", "1", "0");

        ProcessoLocacao p = processos().get(0);
        assertEquals(EtapaLocacao.APROVACAO, p.getEtapaAtual());
        assertTrue(p.getPendencias().contains("Aguardando aprovação da Fundação"));
        assertTrue(saida, saida.contains("movido para: Aprovação"));
    }

    // ---------- cancelar ----------

    @Test
    public void cancelarRemoveOProcesso() {
        salvarProcesso(EtapaLocacao.PROPOSTA, 5000.0);

        String saida = rodar("6", "1", "S", "0");

        assertTrue(processos().isEmpty());
        assertTrue(saida, saida.contains("cancelado e removido"));
    }

    // ---------- painel / detalhes ----------

    @Test
    public void painelMostraProgressoECpfMascarado() {
        salvarProcesso(EtapaLocacao.DOCUMENTOS, 5000.0);

        String saida = rodar("2", "0");

        assertTrue(saida, saida.contains("PAINEL DE LOCAÇÕES"));
        assertTrue(saida, saida.contains("Etapa 2/9"));
        assertTrue(saida, saida.contains("***.231.239-**"));
        assertTrue(saida, saida.contains("[Documentos]"));
    }

    @Test
    public void detalhesMostraPendenciasDoProcesso() {
        salvarProcesso(EtapaLocacao.PROPOSTA, 5000.0, "Aguardando análise da proposta");

        String saida = rodar("3", "1", "0");

        assertTrue(saida, saida.contains("DETALHES DO PROCESSO LOC-001"));
        assertTrue(saida, saida.contains("Aguardando análise da proposta"));
    }
}
