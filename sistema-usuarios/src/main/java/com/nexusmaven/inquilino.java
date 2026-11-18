package com.nexusmaven;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class inquilino extends Usuario {
    private final String tipo = "inquilino";

    public inquilino() {}
    public inquilino(String nome, String email, String senha, String cpf) { super(nome, email, senha, cpf); }

    public String getTipo() { return tipo; }

    public void abrirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===================================");
            System.out.println("        MENU INQUILINO - NEXUS     ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! O que deseja fazer?");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Ver imóveis disponíveis");
            System.out.println("3 - Minhas faturas (Cobranças)");
            System.out.println("4 - Acompanhar minha locação");
            System.out.println("5 - Pagar aluguel (Pix/Transferência)");
            System.out.println("6 - Meus recibos");
            System.out.println("7 - Meus documentos");
            System.out.println("8 - Avisos");
            System.out.println("9 - Falar com a Fundação");
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            if (opcao == EntradaConsole.OPCAO_INVALIDA) {
                System.out.println("❌ Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    verMeusDados();
                    break;
                case 2:
                    verImoveisDisponiveis();
                    break;
                case 3:
                    verMinhasCobrancas();
                    break;
                case 4:
                    verMinhaLocacao();
                    break;
                case 5:
                    exibirComoPagar();
                    break;
                case 6:
                    verMeusRecibos();
                    break;
                case 7:
                    verMeusDocumentos();
                    break;
                case 8:
                    verAvisos();
                    break;
                case 9:
                    falarComAFundacao();
                    break;
                case 0:
                    System.out.println("👋 Saindo...");
                    break;
                default:
                    System.out.println("❌ Opção inválida.");
                    break;
            }
        }
    }

    private void verMeusDados() {
        System.out.println("\n--- SEUS DADOS ---");
        System.out.println("Nome: " + getNome());
        System.out.println("Email: " + getEmail());
        System.out.println("CPF: " + getCpf());
    }

    private void verImoveisDisponiveis() {
        System.out.println("\n--- IMÓVEIS DISPONÍVEIS ---");
        List<Imovel> imoveis = JsonUtil.ler(JsonUtil.ARQUIVO_IMOVEIS, Imovel.class);
        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imóvel cadastrado.");
            return;
        }
        for (Imovel i : imoveis) {
            System.out.println("🏠 " + i.getNome() + " - " + i.getLocalizacao() + " | Aluguel: " + Moeda.formatar(i.getValorAluguel()));
        }
    }

    private void verMinhasCobrancas() {
        System.out.println("\n--- MINHAS FATURAS DE ALUGUEL ---");
        List<Cobranca> cobrancas = JsonUtil.ler(MenuCobrancas.ARQUIVO_COBRANCAS, Cobranca.class);
        
        boolean encontrou = false;
        for (Cobranca c : cobrancas) {
            if (Objects.equals(c.getCpfInquilino(), this.getCpf())) {
                System.out.println("Nº: " + c.getId() + " | Mês: " + c.getMesReferencia() + " | Imóvel: " + c.getNomeImovel());
                System.out.println("   Valor: " + Moeda.formatar(c.getValor()) + " | Vencimento: " + Datas.formatar(c.getVencimento()));
                System.out.println("   Situação: " + c.getSituacao(LocalDate.now()));
                System.out.println("-------------------------------------------------");
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma fatura registrada no seu CPF.");
        }
    }

    private void verMinhaLocacao() {
        System.out.println("\n--- MEU PROCESSO DE LOCAÇÃO ---");
        List<ProcessoLocacao> processos = JsonUtil.ler(LocacaoService.ARQUIVO_LOCACOES, ProcessoLocacao.class);

        boolean encontrou = false;
        for (ProcessoLocacao p : processos) {
            if (Objects.equals(p.getCpfInquilino(), this.getCpf())) {
                EtapaLocacao etapa = p.getEtapaAtual();
                System.out.println("Processo: " + p.getId() + " | Imóvel: " + p.getNomeImovel());
                System.out.println("Etapa atual: " + (etapa == null ? "Desconhecida" : etapa.getRotulo()));
                System.out.println("Pendências:");
                if (p.getPendencias().isEmpty()) {
                    System.out.println("  - Nenhuma (Tudo certo!)");
                } else {
                    p.getPendencias().forEach(pend -> System.out.println("  - " + pend));
                }
                System.out.println("-------------------------------------------------");
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Você ainda não possui nenhum processo de locação em andamento.");
        }
    }

    // --- NOVAS FUNÇÕES ADAPTADAS (SEM O PAINELINQUILINO) ---

    private void exibirComoPagar() {
        System.out.println("\n--- COMO PAGAR O ALUGUEL ---");
        System.out.println("1) Em dinheiro, na Secretaria");
        System.out.println("   Rua da Fundação, 100, Centro (Segunda a sexta, 8h às 17h)");
        System.out.println("2) Pix");
        System.out.println("   Chave Pix (CNPJ): 00.000.000/0001-00");
        System.out.println("3) Transferência");
        System.out.println("   Banco do Brasil | Agência 0000-0 | Conta 00000-0");
        System.out.println("\n(Após pagar por Pix ou Transferência, envie o comprovante na opção 'Meus documentos').");
    }

    private void verMeusRecibos() {
        System.out.println("\n--- MEUS RECIBOS (ALUGUÉIS PAGOS) ---");
        List<Cobranca> cobrancas = JsonUtil.ler(MenuCobrancas.ARQUIVO_COBRANCAS, Cobranca.class);
        
        boolean encontrou = false;
        for (Cobranca c : cobrancas) {
            if (Objects.equals(c.getCpfInquilino(), this.getCpf()) && c.getDataPagamento() != null) {
                System.out.println("🧾 Recibo Nº: " + c.getNumeroRecibo() + " | Mês: " + c.getMesReferencia());
                System.out.println("   Valor pago: " + Moeda.formatar(c.getValorPago()) + " em " + Datas.formatar(c.getDataPagamento()));
                System.out.println("-------------------------------------------------");
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum recibo encontrado. Seus pagamentos aparecerão aqui.");
        }
    }

    private void verMeusDocumentos() {
        System.out.println("\n--- MEUS DOCUMENTOS ---");
        System.out.println("📄 Contrato de locação: Guardado na Secretaria.");
        System.out.println("📋 Termo de vistoria: Guardado na Secretaria.");
        System.out.println("(Para enviar um comprovante de pagamento, entregue na Secretaria ou avise o Admin).");
    }

    private void verAvisos() {
        System.out.println("\n--- AVISOS DA FUNDAÇÃO ---");
        System.out.println("Nenhum aviso novo no momento.");
        System.out.println("(Avisos de manutenção ou reajuste aparecerão aqui).");
    }

    private void falarComAFundacao() {
        System.out.println("\n--- FALAR COM A FUNDAÇÃO ---");
        System.out.println("📞 Telefone: (79) 99999-9999");
        System.out.println("💬 WhatsApp: (79) 99999-9999");
        System.out.println("📍 Endereço: Rua da Fundação, 100, Centro.");
        System.out.println("   Horário de atendimento: Segunda a sexta, das 8h às 17h.");
    }
}
