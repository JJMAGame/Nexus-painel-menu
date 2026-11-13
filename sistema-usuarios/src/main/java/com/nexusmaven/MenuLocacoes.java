package com.nexusmaven;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuLocacoes {

    private final Scanner scanner;
    private final LocacaoService service = new LocacaoService();

    // ⚠️ O CONSTRUTOR TEM QUE ESTAR EXATAMENTE ASSIM:
    public MenuLocacoes(Scanner scanner) {
        this.scanner = scanner;
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ PROCESSOS DE LOCAÇÃO ------");
            System.out.println("1 - Iniciar nova proposta de locação");
            System.out.println("2 - Ver painel de locações");
            System.out.println("3 - Avançar etapa de um processo");
            System.out.println("4 - Gerenciar pendências");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: novaProposta(); break;
                    case 2: listar(); break;
                    case 3: avancarEtapa(); break;
                    case 4: gerenciarPendencias(); break;
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void novaProposta() {
        List<Usuario> usuarios = JsonUtil.ler(JsonUtil.ARQUIVO_USUARIOS, Usuario.class);
        List<Imovel> imoveis = JsonUtil.ler(JsonUtil.ARQUIVO_IMOVEIS, Imovel.class);
        
        List<Usuario> inquilinos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof inquilino) inquilinos.add(u);
        }

        if (inquilinos.isEmpty() || imoveis.isEmpty()) {
            System.out.println("❌ É preciso ter pelo menos um inquilino e um imóvel cadastrados.");
            return;
        }

        // 1. Escolhe o Inquilino
        int iInq = EntradaTexto.escolher(scanner, "\nQual inquilino?", inquilinos, 
                u -> u.getNome() + " (CPF: " + u.getCpf() + ")");
        if (iInq < 0) return;
        Usuario inquilinoEscolhido = inquilinos.get(iInq);

        // 2. Escolhe o Imóvel
        int iImo = EntradaTexto.escolher(scanner, "\nQual imóvel?", imoveis, 
                i -> i.getNome() + " - " + Moeda.formatar(i.getValorAluguel()));
        if (iImo < 0) return;
        Imovel imovelEscolhido = imoveis.get(iImo);

        // 3. Pede a renda mensal (Regra de negócio)
        String rendaTexto = EntradaTexto.perguntar(scanner, "Renda mensal do inquilino: R$ ");
        if (rendaTexto == null) return;
        double renda = Moeda.ler(rendaTexto);

        // 4. Cria o processo
        String id = service.gerarProximoId();
        ProcessoLocacao novo = new ProcessoLocacao(id, inquilinoEscolhido.getCpf(), 
                imovelEscolhido.getNome(), EtapaLocacao.PROPOSTA, renda);
        
        novo.adicionarPendencia("Aguardando análise da proposta");
        service.cadastrar(novo);

        System.out.println("\n✅ Proposta " + id + " criada na etapa: " + novo.getEtapaAtual().getRotulo());
    }

    private void listar() {
        List<ProcessoLocacao> processos = JsonUtil.ler(LocacaoService.ARQUIVO_LOCACOES, ProcessoLocacao.class);
        System.out.println("\n--- PAINEL DE LOCAÇÕES ---");
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo cadastrado.");
            return;
        }
        for (ProcessoLocacao p : processos) {
            String pend = p.getPendencias().isEmpty() ? "Nenhuma" : p.getPendencias().get(0);
            System.out.printf("[%s] CPF Inquilino: %s | Imóvel: %s | Etapa: %s | Pendência: %s%n",
                    p.getId(), p.getCpfInquilino(), p.getNomeImovel(), p.getEtapaAtual().getRotulo(), pend);
        }
    }

    private void avancarEtapa() {
        ProcessoLocacao p = escolherProcesso();
        if (p == null) return;

        if (!p.getPendencias().isEmpty()) {
            System.out.println("❌ Resolva as pendências antes de avançar a etapa.");
            return;
        }

        EtapaLocacao atual = p.getEtapaAtual();
        if (atual == EtapaLocacao.CONCLUIDA) {
            System.out.println("❌ Este processo já está concluído.");
            return;
        }

        // REGRA DE NEGÓCIO: Análise de Renda (Deve ser 3x o valor do aluguel)
        if (atual == EtapaLocacao.ANALISE_RENDA) {
            Imovel imovel = buscarImovel(p.getNomeImovel());
            if (imovel != null && p.getRendaMensal() < (imovel.getValorAluguel() * 3)) {
                System.out.println("❌ Renda insuficiente! A renda deve ser pelo menos 3x o valor do aluguel.");
                return;
            }
        }

        // Avança o Enum
        EtapaLocacao[] etapas = EtapaLocacao.values();
        EtapaLocacao proxima = etapas[atual.ordinal() + 1];
        
        p.setEtapaAtual(proxima);
        service.atualizar(p);
        System.out.println("✅ Processo avançado para: " + proxima.getRotulo());
    }

    private void gerenciarPendencias() {
        ProcessoLocacao p = escolherProcesso();
        if (p == null) return;

        if (p.getPendencias().isEmpty()) {
            System.out.println("✅ Este processo não possui pendências.");
            return;
        }

        System.out.println("\nPendências atuais:");
        for (int i = 0; i < p.getPendencias().size(); i++) {
            System.out.println((i + 1) + " - " + p.getPendencias().get(i));
        }

        String resp = EntradaTexto.perguntar(scanner, "Digite o número da pendência para resolver (ou Enter para voltar): ");
        if (resp == null || resp.isEmpty()) return;
        
        try {
            int index = Integer.parseInt(resp) - 1;
            String pendenciaResolvida = p.getPendencias().remove(index);
            service.atualizar(p);
            System.out.println("✅ Pendência resolvida: " + pendenciaResolvida);
        } catch (Exception e) {
            System.out.println("❌ Número inválido.");
        }
    }

    private ProcessoLocacao escolherProcesso() {
        List<ProcessoLocacao> processos = JsonUtil.ler(LocacaoService.ARQUIVO_LOCACOES, ProcessoLocacao.class);
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo cadastrado.");
            return null;
        }
        int i = EntradaTexto.escolher(scanner, "Qual processo?", processos, 
                p -> p.getId() + " - " + p.getNomeImovel() + " (" + p.getEtapaAtual().getRotulo() + ")");
        return i >= 0 ? processos.get(i) : null;
    }

    private Imovel buscarImovel(String nome) {
        return JsonUtil.ler(JsonUtil.ARQUIVO_IMOVEIS, Imovel.class).stream()
                .filter(i -> i.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);
    }
}
