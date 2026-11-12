package com.nexusmaven;

import java.util.List;
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

    // ⚠️ NOVO MÉTODO INTEGRADO AO COBRANCAS.JSON
    private void verMinhasCobrancas() {
        System.out.println("\n--- MINHAS FATURAS DE ALUGUEL ---");
        List<Cobranca> cobrancas = JsonUtil.ler(MenuCobrancas.ARQUIVO_COBRANCAS, Cobranca.class);
        
        boolean encontrou = false;
        for (Cobranca c : cobrancas) {
            if (c.getCpfInquilino().equals(this.getCpf())) {
                System.out.println("Nº: " + c.getId() + " | Mês: " + c.getMesReferencia() + " | Imóvel: " + c.getNomeImovel());
                System.out.println("   Valor: " + Moeda.formatar(c.getValor()) + " | Vencimento: " + Datas.formatar(c.getVencimento()));
                System.out.println("   Situação: " + c.getSituacao(java.time.LocalDate.now()));
                System.out.println("-------------------------------------------------");
                encontrou = true;
            }
        }
        
        if (!encontrou) {
            System.out.println("Nenhuma fatura registrada no seu CPF.");
        }
    }
}
