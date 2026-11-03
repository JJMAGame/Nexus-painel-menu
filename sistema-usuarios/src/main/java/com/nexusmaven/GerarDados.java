package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha, cpf;
        System.out.println("⏳ Gerando novos dados de usuarios...");
        Scanner scanner = new Scanner(System.in);

        //Qual tipo de dados vai ser gerado:
        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite o CPF do usuário: "); // ⚠️ NOVO
               cpf = scanner.nextLine();                      // ⚠️ NOVO
               System.out.print("Digite a senha do usuário (mínimo 6 e máximo 12 caracteres):");
               senha = scanner.nextLine();

               System.out.println("\nQual o tipo de conta?");
               System.out.println("1 - Inquilino (Padrão)");
               System.out.println("2 - Administrador");
               System.out.print("Escolha o tipo: ");
               String tipoConta = scanner.nextLine();

           try{
               Usuario u = null;

               if (tipoConta.equals("2")) {
                  u = new admin(nome, email, senha, cpf);     // ⚠️ Passando o CPF
               } else {
                  u = new inquilino(nome, email, senha, cpf); // ⚠️ Passando o CPF
               }
               
               List<Usuario> usuarios = JsonUtil.ler("usuarios.json", Usuario.class);
               usuarios.add(u);
   
               JsonUtil.salvar("usuarios.json", usuarios);
   
               String tipoExibicao = tipoConta.equals("2") ? "Administrador" : "Inquilino";
               System.out.println("Dados de usuário gerados e salvos com sucesso! do tipo " + tipoExibicao);
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
            System.out.print("Digite o CPF do proprietário: "); // ⚠️ NOVO
            String cpfProprietario = scanner.nextLine();        // ⚠️ NOVO

            try{
                // Passando o CPF para o Imóvel
                Imovel imovel = new Imovel(lnome, localizacao, cpfProprietario);
   
               List<Imovel> imoveis = JsonUtil.ler("imoveis.json", Imovel.class);
               imoveis.add(imovel);
   
               JsonUtil.salvar("imoveis.json", imoveis);
   
               System.out.println("Dados de imóvel gerados e salvos com sucesso!");
               System.out.println("Total de imóveis no sistema: " + imoveis.size());
   
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }
    }
}
