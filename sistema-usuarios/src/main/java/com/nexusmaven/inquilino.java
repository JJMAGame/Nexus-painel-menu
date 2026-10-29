package com.nexusmaven;

import java.util.Scanner;

public class inquilino extends Usuario {
    private final String tipo = "inquilino";

    public inquilino() {} // Obrigatório para o Gson

    public inquilino(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public String getTipo() { return tipo; }

	    public void abrirMenu(Scanner scanner) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("        MENU INQUILINO - NEXUS     ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! O que deseja fazer?");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Ver imóveis disponíveis");
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
                    System.out.println("\n--- SEUS DADOS ---");
                    System.out.println("Nome: " + getNome());
                    System.out.println("Email: " + getEmail());
                    break;
                case 2:
                    System.out.println("\n--- IMÓVEIS DISPONÍVEIS ---");
                    // Aqui você pode ler o imoveis.json e listar
                    System.out.println("(Listagem de imóveis em desenvolvimento...)");
                    break;
                case 0:
                    System.out.println("👋 Saindo do menu do inquilino...");
                    break;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }
}
