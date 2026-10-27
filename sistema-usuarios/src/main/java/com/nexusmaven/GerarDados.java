package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha;
        System.out.println("⏳ Gerando novos dados de usuarios...");
        Scanner scanner = new Scanner(System.in);

        // Cria o usuário com os dados fornecidos pelo usuário
        System.out.print("Digite o nome do usuário: ");
        nome = scanner.nextLine();
        System.out.print("Digite o email do usuário: ");
        email = scanner.nextLine();
        System.out.print("Digite a senha do usuário (mínimo 6 e máximo 12 caracteres):");
        senha = scanner.nextLine();

        scanner.close();

        try{
            Usuario u = new Usuario(nome, email, senha);

            List<Usuario> usuarios = JsonUtil.lerUsuarios();
            usuarios.add(u);

            JsonUtil.salvarUsuarios(usuarios);

            System.out.println("Dados de usuário gerados e salvos com sucesso!");
            System.out.println("Total de usuários no sistema: " + usuarios.size());

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }
}
