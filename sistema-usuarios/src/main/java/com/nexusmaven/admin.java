package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class Admin extends Usuario {
    private final String tipo = "admin";

    public Admin() {}
    public Admin(String nome, String email, String senha, String cpf) { super(nome, email, senha, cpf); }

    public String getTipo() { return tipo; }

    public void abrirMenu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===================================");
            System.out.println("         MENU ADMIN - NEXUS        ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! O que deseja fazer?");
            System.out.println("1 - Gerar novos dados (Usuários/Imóveis)");
            System.out.println("2 - Registrar pagamentos (Financeiro)");
            System.out.println("3 - Ver todas as receitas"); // BÔNUS
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            if (opcao == EntradaConsole.OPCAO_INVALIDA) {
                System.out.println("❌ Digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    GerarDados.executar(scanner); // Puxa os dados
                    break;
                case 2:
                    registrarPagamento(scanner);  // ⚠️ NOVO
                    break;
                case 3:
                    verReceitas();                // ⚠️ NOVO
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

    private void registrarPagamento(Scanner scanner) {
        System.out.println("\n--- REGISTRO FINANCEIRO ---");
        System.out.print("CPF do Inquilino: ");
        String cpf = scanner.nextLine();
        System.out.print("Nome do Local: ");
        String local = scanner.nextLine();
        System.out.print("Mês de referência (ex: 11/2026): ");
        String mes = scanner.nextLine();

        try {
            System.out.print("Valor do Aluguel: R$ ");
            double aluguel = Double.parseDouble(scanner.nextLine());
            System.out.print("Água: R$ ");
            double agua = Double.parseDouble(scanner.nextLine());
            System.out.print("Energia: R$ ");
            double energia = Double.parseDouble(scanner.nextLine());
            System.out.print("Manutenção: R$ ");
            double manutencao = Double.parseDouble(scanner.nextLine());

            Financeiro fin = new Financeiro(cpf, local, aluguel, agua, energia, manutencao, mes);

            List<Financeiro> listaFin = JsonUtil.ler("financeiro.json", Financeiro.class);
            listaFin.add(fin);
            JsonUtil.salvar("financeiro.json", listaFin);

            System.out.println("\n✅ Registro salvo! Total: R$ " + fin.calcularTotal());
        } catch (NumberFormatException e) {
            System.out.println("❌ Erro: Digite valores numéricos (use ponto para centavos).");
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