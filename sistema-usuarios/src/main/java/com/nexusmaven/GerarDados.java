package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha, cpf;
        System.out.println("⏳ Gerando novos dados...");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite o CPF do usuário: "); 
               cpf = scanner.nextLine();                      
               System.out.print("Digite a senha (mínimo 6 e máximo 12 caracteres): ");
               senha = scanner.nextLine();

               System.out.println("\nQual o tipo de conta?");
               System.out.println("1 - Inquilino (Padrão)");
               System.out.println("2 - Administrador");
               System.out.print("Escolha o tipo: ");
               String tipoConta = scanner.nextLine();

           try{
               Usuario u = null;

               if (tipoConta.equals("2")) {
                  u = new admin(nome, email, senha, cpf);     
               } else {
                  u = new inquilino(nome, email, senha, cpf); 
               }
               
               List<Usuario> usuarios = JsonUtil.ler("usuarios.json", Usuario.class);
               usuarios.add(u);
   
               JsonUtil.salvar("usuarios.json", usuarios);
   
               String tipoExibicao = tipoConta.equals("2") ? "Administrador" : "Inquilino";
               System.out.println("✅ Usuário do tipo " + tipoExibicao + " salvo com sucesso!");
               System.out.println("Total de usuários no sistema: " + usuarios.size());
   
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }
        
        else if (escolha.equals("2")) {
            System.out.print("Digite o nome do imóvel: ");
            String lnome = scanner.nextLine();
            System.out.print("Digite a localização do imóvel: ");
            String localizacao = scanner.nextLine();
            System.out.print("Digite o CPF do proprietário: "); 
            String cpfProprietario = scanner.nextLine();        

            // ⚠️ NOVO: Pedindo o valor do aluguel
            System.out.print("Digite o valor do aluguel (ex: 1500.50): ");
            double valorAluguel = 0;
            try {
                valorAluguel = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Erro: Valor do aluguel inválido. Use ponto para centavos.");
                return; // Cancela o cadastro
            }

            try{
                // Passando o valor para o Imóvel
                Imovel imovel = new Imovel(lnome, localizacao, cpfProprietario, valorAluguel);
   
               List<Imovel> imoveis = JsonUtil.ler("imoveis.json", Imovel.class);
               imoveis.add(imovel);
   
               JsonUtil.salvar("imoveis.json", imoveis);
   
               System.out.println("✅ Imóvel salvo com sucesso! Aluguel: R$" + valorAluguel);
               System.out.println("Total de imóveis no sistema: " + imoveis.size());
   
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }
            else if (escolha.equals("3")) {
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
        }
    }
  }