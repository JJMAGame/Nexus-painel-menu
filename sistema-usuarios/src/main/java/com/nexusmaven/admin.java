package com.nexusmaven;

import java.util.Scanner;

public class admin extends Usuario {
    private final String tipo = "admin";

    public admin() {} // Obrigatório para o Gson

    public admin(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public String getTipo() { return tipo; }

        public void abrirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("         MENU ADMIN - NEXUS        ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! Bem-vindo ao painel de administração.");
            System.out.println("1 - Gerar novos dados (Usuários/Imóveis)");
            System.out.println("2 - Registrar pagamentos");
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    // Chama a classe que gera dados (que também tem o seu próprio Scanner)
                    GerarDados.main(null); 
                    break;
                case 2:
                    System.out.println("💰 Registrar pagamentos - funcionalidade em desenvolvimento.");
                    break;
                case 0:
                    System.out.println("👋 Saindo do menu admin...");
                    break;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }
}
