package com.nexusmaven;

import java.util.Scanner;
import java.util.List;

public class admin extends Usuario {
    private final String tipo = "admin";

    public admin() {}
    public admin(String nome, String email, String senha, String cpf) { super(nome, email, senha, cpf); }

    public String getTipo() { return tipo; }

    public void abrirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===================================");
            System.out.println("         MENU ADMIN - NEXUS        ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! O que deseja fazer?");
            System.out.println("1 - Gerar novos dados (Usuários/Imóveis)");
            System.out.println("2 - Solicitações de pagamento (Cobranças)");
            System.out.println("3 - Despesas da Fundação");
            System.out.println("4 - Imóveis");
            System.out.println("5 - Acessos (funcionários e inquilinos)");
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            if (opcao == EntradaConsole.OPCAO_INVALIDA) {
                System.out.println("❌ Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    GerarDados.executar(scanner); 
                    break;
                case 2:
                    new MenuCobrancas(scanner).abrir();
                    break;
                case 3:
                    new MenuDespesas(scanner, getNome()).abrir();
                    break;
                case 4:
                    new MenuImoveis(scanner).abrir();
                    break;
                case 5:
                    new MenuAcessos(scanner, getEmail()).abrir();
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

    private void verReceitas() {
        System.out.println("\n--- EXTRATO DE RECEITAS ---");
        List<Financeiro> listaFin = JsonUtil.ler("financeiro.json", Financeiro.class);
        if (listaFin.isEmpty()) {
            System.out.println("Nenhum pagamento registrado ainda.");
            return;
        }
        double totalGeral = 0;
        for (Financeiro f : listaFin) {
            System.out.println(f.toString());
            totalGeral += f.calcularTotal();
        }
        System.out.println("=================================");
        System.out.println("💵 RECEITA TOTAL ACUMULADA: R$ " + totalGeral);
        System.out.println("=================================");
    }
}