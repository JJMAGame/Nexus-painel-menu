package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class admin extends Usuario {
    private final String tipo = "admin";

    public admin() {} // Obrigatório para o Gson

    public admin(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
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
            System.out.println("3 - Receita do mes");
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
                     System.out.println("\n--- REGISTRO FINANCEIRO ---");
                     System.out.print("Digite o CPF do Inquilino: ");
                     String cpfInquilino = scanner.nextLine();
                     
                     System.out.print("Digite o nome do Local (Imóvel): ");
                     String nomeLocal = scanner.nextLine();
      
                     System.out.print("Digite o mês de referência (ex: 10/2026): ");
                     String mes = scanner.nextLine();

                    try {
                        System.out.print("Valor do Aluguel: R$ ");
                        double aluguel = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Valor da Água: R$ ");
                        double agua = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Valor da Energia: R$ ");
                        double energia = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Valor da Manutenção: R$ ");
                        double manutencao = Double.parseDouble(scanner.nextLine());
        
                        Financeiro fin = new Financeiro(cpfInquilino, nomeLocal, aluguel, agua, energia, manutencao, mes);
        
                        List<Financeiro> listaFin = JsonUtil.ler("financeiro.json", Financeiro.class);
                        listaFin.add(fin);
                        JsonUtil.salvar("financeiro.json", listaFin);
        
                        System.out.println("\n✅ Registro financeiro salvo com sucesso!");
                        System.out.println("Total gerado neste mês: R$ " + fin.calcularTotal());
        
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Erro: Digite apenas números para os valores (use ponto para centavos).");
                    }
                    break;
                case 3:
                    System.out.println("📊 Receita do mês - funcionalidade em desenvolvimento.");
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
