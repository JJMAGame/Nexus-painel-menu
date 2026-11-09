package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        try (Scanner scanner = EntradaConsole.criarScanner()) {
            executar(scanner, JsonUtil.ARQUIVO_USUARIOS);
        }
    }

    // Separado do main para poder ser testado com outro banco e outra entrada
    static void executar(Scanner scanner, String arquivoUsuarios) {
        System.out.println("=================================");
        System.out.println("      TELA DE LOGIN - NEXUS      ");
        System.out.println("=================================");

        Usuario usuarioLogado = fazerLogin(scanner, arquivoUsuarios);
        if (usuarioLogado == null) {
            return;
        }

        // ===== CHAMANDO O MENU DO TIPO DE USUÁRIO =====
        if (usuarioLogado instanceof admin admin) {
            admin.abrirMenu(scanner);
        } else if (usuarioLogado instanceof inquilino inquilino) {
            inquilino.abrirMenu(scanner);
        } else {
            System.out.println("Tipo de usuário desconhecido. Encerrando o programa.");
        }
    }

    /** Repete o login até acertar. Devolve null se não há usuários ou se a entrada terminar. */
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

            System.out.print("Senha: ");
            if (!scanner.hasNextLine()) return null;
            String senhaDigitada = scanner.nextLine();

            Usuario encontrado = buscarPorEmail(usuarios, emailDigitado);

            if (encontrado == null) {
                System.out.println("\n Email não encontrado no sistema. Tente novamente.\n");
            } else if (encontrado.autenticar(emailDigitado, senhaDigitada)) {
                System.out.println("\n Login realizado com sucesso! Bem-vindo, " + encontrado.getNome() + ".");
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
