package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        try (Scanner scanner = EntradaConsole.criarScanner()) {
            executar(scanner, JsonUtil.ARQUIVO_USUARIOS);
        }
    }

    static void executar(Scanner scanner, String arquivoUsuarios) {
        // ⚠️ O laço infinito mantém o programa rodando até o usuário fechar (Ctrl+C)
        while (true) {
            System.out.println("\n=================================");
            System.out.println("      TELA DE LOGIN - NEXUS      ");
            System.out.println("=================================");

            Usuario usuarioLogado = fazerLogin(scanner, arquivoUsuarios);
            
            // Se o fazerLogin retornar null (ex: não há usuários cadastrados), encerra o programa
            if (usuarioLogado == null) {
                break;
            }

            // ===== CHAMANDO O MENU DO TIPO DE USUÁRIO =====
            if (usuarioLogado instanceof admin admin) {
                admin.abrirMenu(scanner); 
                // Quando o admin sair do abrirMenu, o código continua para a próxima linha
                // e o while(true) volta para o topo, mostrando a tela de login novamente.
            } else if (usuarioLogado instanceof inquilino inquilino) {
                inquilino.abrirMenu(scanner);
            } else {
                System.out.println("Tipo de usuário desconhecido.");
            }
            
            System.out.println("🔄 Retornando para a tela de login...");
        }
        
        System.out.println("👋 Sistema encerrado. Até logo!");
    }

    static Usuario fazerLogin(Scanner scanner, String arquivoUsuarios) {
        List<Usuario> usuarios = JsonUtil.ler(arquivoUsuarios, Usuario.class);

        if (usuarios.isEmpty()) {
            System.out.println("\n Nenhum usuário encontrado no JSON. Rode o GerarDados primeiro.\n");
            return null;
        }

        while (true) {
            System.out.print("Email: ");
            if (!scanner.hasNextLine()) return null;
            String emailDigitado = scanner.nextLine().trim();

            if (emailDigitado.isEmpty()) {
                System.out.println("Você precisa fornecer o email.");
                continue; 
            }

            System.out.print("Senha: ");
            if (!scanner.hasNextLine()) return null;
            String senhaDigitada = scanner.nextLine();

            if (senhaDigitada.isEmpty()) {
                System.out.println("Você precisa fornecer a senha.");
                continue; 
            }

            Usuario encontrado = buscarPorEmail(usuarios, emailDigitado);

            if (encontrado == null) {
                System.out.println("\n Email não encontrado no sistema. Tente novamente.\n");
                continue; 
            }

            if (encontrado.autenticar(emailDigitado, senhaDigitada)) {
                System.out.println("\n ✅ Login realizado com sucesso! Bem-vindo, " + encontrado.getNome() + ".");
                return encontrado;
            } else {
                System.out.println("\n Senha incorreta. Tente novamente.\n");
            }
        }
    }

    private static Usuario buscarPorEmail(List<Usuario> usuarios, String email) {
        for (Usuario u : usuarios) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return u;
            }
        }
        return null;
    }
}