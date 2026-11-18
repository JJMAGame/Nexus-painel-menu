package com.nexusmaven;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuLocacoes {

    private final Scanner scanner;
    private final String arquivoUsuarios;
    private final String arquivoImoveis;
    private final LocacaoService service;

    public MenuLocacoes(Scanner scanner) {
        this(scanner, JsonUtil.ARQUIVO_USUARIOS, JsonUtil.ARQUIVO_IMOVEIS, LocacaoService.ARQUIVO_LOCACOES);
    }

    MenuLocacoes(Scanner scanner, String arquivoUsuarios, String arquivoImoveis, String arquivoLocacoes) {
        this.scanner = scanner;
        this.arquivoUsuarios = arquivoUsuarios;
        this.arquivoImoveis = arquivoImoveis;
        this.service = new LocacaoService(arquivoLocacoes);
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ PROCESSOS DE LOCAÇÃO ------");
            System.out.println("1 - Iniciar nova proposta de locação");
            System.out.println("2 - Ver painel de locações");
            System.out.println("3 - Ver detalhes de um processo");
            System.out.println("4 - Mudar etapa (avançar ou voltar)");
            System.out.println("5 - Gerenciar pendências");
            System.out.println("6 - Cancelar processo");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: novaProposta(); break;
                    case 2: listar(); break;
                    case 3: detalhar(); break;
                    case 4: mudarEtapa(); break;
                    case 5: gerenciarPendencias(); break;
                    case 6: cancelar(); break;
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void novaProposta() {
        List<Usuario> usuarios = JsonUtil.ler(arquivoUsuarios, Usuario.class);
        List<Imovel> imoveis = JsonUtil.ler(arquivoImoveis, Imovel.class);

        List<Usuario> inquilinos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof inquilino) inquilinos.add(u);
        }

        if (inquilinos.isEmpty() || imoveis.isEmpty()) {
            System.out.println("❌ É preciso ter pelo menos um inquilino e um imóvel cadastrados.");
            return;
        }

        int iInq = EntradaTexto.escolher(scanner, "\nQual inquilino?", inquilinos,
                u -> u.getNome() + " (CPF: " + u.getCpf() + ")");
        if (iInq < 0) return;
        Usuario inquilinoEscolhido = inquilinos.get(iInq);

        int iImo = EntradaTexto.escolher(scanner, "\nQual imóvel?", imoveis,
                i -> i.getNome() + " - " + Moeda.formatar(i.getValorAluguel()));
        if (iImo < 0) return;
        Imovel imovelEscolhido = imoveis.get(iImo);

        String rendaTexto = EntradaTexto.perguntar(scanner, "Renda mensal do inquilino: R$ ");
        if (rendaTexto == null) return;
        double renda = Moeda.ler(rendaTexto);

        String id = service.gerarProximoId();
        ProcessoLocacao novo = new ProcessoLocacao(id, inquilinoEscolhido.getCpf(),
                imovelEscolhido.getNome(), EtapaLocacao.PROPOSTA, renda);

        String pendenciaInicial = EtapaLocacao.PROPOSTA.pendenciaPadrao();
        if (pendenciaInicial != null) novo.adicionarPendencia(pendenciaInicial);
        service.cadastrar(novo);

        System.out.println("\n✅ Proposta " + id + " criada na etapa: " + novo.getEtapaAtual().getRotulo());
    }

    private void listar() {
        List<ProcessoLocacao> processos = service.listar();
        System.out.println("\n--- PAINEL DE LOCAÇÕES ---");
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo cadastrado.");
            return;
        }
        for (ProcessoLocacao p : processos) {
            EtapaLocacao atual = p.getEtapaAtual();
            String pend = p.getPendencias().isEmpty() ? "Nenhuma" : p.getPendencias().get(0);
            System.out.println("[" + p.getId() + "] " + p.getNomeImovel()
                    + " (CPF " + MenuAcessos.mascararCpf(p.getCpfInquilino()) + ")");
            System.out.println("   Etapa " + (atual == null ? "?" : (atual.ordinal() + 1))
                    + "/" + EtapaLocacao.values().length + " | " + trilha(atual));
            System.out.println("   Pendência: " + pend);
        }
    }

    private void detalhar() {
        ProcessoLocacao p = escolherProcesso();
        if (p == null) return;
        EtapaLocacao atual = p.getEtapaAtual();

        System.out.println("\n--- DETALHES DO PROCESSO " + p.getId() + " ---");
        System.out.println("Imóvel: " + p.getNomeImovel());
        System.out.println("Inquilino (CPF): " + MenuAcessos.mascararCpf(p.getCpfInquilino()));
        System.out.println("Renda mensal: " + Moeda.formatar(p.getRendaMensal()));
        System.out.println("Etapa atual: " + (atual == null ? "Desconhecida" : atual.getRotulo()));
        System.out.println("Trilha: " + trilha(atual));
        System.out.println("Pendências:");
        if (p.getPendencias().isEmpty()) {
            System.out.println("  - Nenhuma (Tudo certo!)");
        } else {
            for (int i = 0; i < p.getPendencias().size(); i++) {
                System.out.println("  " + (i + 1) + " - " + p.getPendencias().get(i));
            }
        }
    }

    private void mudarEtapa() {
        ProcessoLocacao p = escolherProcesso();
        if (p == null) return;

        EtapaLocacao atual = p.getEtapaAtual();
        if (atual == null) {
            System.out.println("❌ Este processo tem uma etapa inválida. Verifique o arquivo locacoes.json.");
            return;
        }

        int dir = EntradaTexto.escolher(scanner, "\nO que deseja fazer?",
                List.of("Avançar etapa", "Voltar etapa"), s -> s);
        if (dir < 0) return;
        boolean avancar = dir == 0;

        EtapaLocacao destino = avancar ? atual.proxima() : atual.anterior();
        if (destino == null) {
            System.out.println(avancar
                    ? "❌ Este processo já está na última etapa (Concluída)."
                    : "❌ Este processo já está na primeira etapa (Proposta).");
            return;
        }

        if (avancar && !p.getPendencias().isEmpty()) {
            System.out.println("❌ Resolva as pendências antes de avançar a etapa.");
            return;
        }

        // ✅ MELHORIA: Regra de negócio: só sai da análise de renda com renda >= 3x o aluguel
        if (avancar && atual == EtapaLocacao.ANALISE_RENDA) {
            Imovel imovel = buscarImovel(p.getNomeImovel());
            if (imovel != null && p.getRendaMensal() < imovel.getValorAluguel() * 3) {
                System.out.println("❌ Renda insuficiente! A renda deve ser pelo menos 3x o valor do aluguel.");
                return;
            }
        }

        p.setEtapaAtual(destino);
        if (avancar) {
            String pendencia = destino.pendenciaPadrao();
            if (pendencia != null) p.adicionarPendencia(pendencia);
        }
        service.atualizar(p);
        System.out.println("✅ Processo " + p.getId() + " movido para: " + destino.getRotulo());
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

        // ✅ MELHORIA: Tratamento específico para índices inválidos e uso do método seguro
        try {
            int index = Integer.parseInt(resp) - 1;
            String pendenciaSelecionada = p.getPendencias().get(index);
            p.resolverPendencia(pendenciaSelecionada);
            service.atualizar(p);
            System.out.println("✅ Pendência resolvida: " + pendenciaSelecionada);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("❌ Número inválido.");
        }
    }

    private void cancelar() {
        ProcessoLocacao p = escolherProcesso();
        if (p == null) return;

        if (!EntradaTexto.confirmar(scanner, "Cancelar o processo " + p.getId() + " (" + p.getNomeImovel()
                + ")? Esta ação não pode ser desfeita")) {
            System.out.println("Nada foi alterado.");
            return;
        }
        service.remover(p.getId());
        System.out.println("✅ Processo " + p.getId() + " cancelado e removido.");
    }

    private ProcessoLocacao escolherProcesso() {
        List<ProcessoLocacao> processos = service.listar();
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo cadastrado.");
            return null;
        }
        int i = EntradaTexto.escolher(scanner, "Qual processo?", processos, p -> {
            EtapaLocacao etapa = p.getEtapaAtual();
            return p.getId() + " - " + p.getNomeImovel() + " (" + (etapa == null ? "?" : etapa.getRotulo()) + ")";
        });
        return i >= 0 ? processos.get(i) : null;
    }

    private Imovel buscarImovel(String nome) {
        return JsonUtil.ler(arquivoImoveis, Imovel.class).stream()
                .filter(i -> i.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);
    }

    /** Monta a trilha visual das etapas: concluídas ✓, atual entre [ ], futuras sem marca. */
    private static String trilha(EtapaLocacao atual) {
        EtapaLocacao[] etapas = EtapaLocacao.values();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < etapas.length; i++) {
            EtapaLocacao e = etapas[i];
            if (i > 0) sb.append(" → ");
            if (atual != null && e.ordinal() < atual.ordinal()) {
                sb.append("✓").append(e.getRotulo());
            } else if (e == atual) {
                sb.append("[").append(e.getRotulo()).append("]");
            } else {
                sb.append(e.getRotulo());
            }
        }
        return sb.toString();
    }
}