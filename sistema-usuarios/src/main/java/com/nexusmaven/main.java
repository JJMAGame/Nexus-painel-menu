package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("      TELA DE LOGIN - NEXUS      ");
        System.out.println("=================================");

        boolean logado = false;

        Usuario usuarioLogado = null; // Variável para armazenar o usuário logado

        //loop de login
        while (!logado) {
            System.out.print("Email: ");
            String emailDigitado = scanner.nextLine();

            System.out.print("Senha: ");
            String senhaDigitada = scanner.nextLine();

            List<Usuario> usuarios = JsonUtil.ler("usuarios.json", Usuario.class);

            if (usuarios.isEmpty()) {
                System.out.println("\n Nenhum usuário encontrado no JSON. Rode o GerarDados primeiro.\n");
                break;
            }

            boolean encontrouEmail = false;

            // 2. Procura o usuário na lista
            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(emailDigitado)) {
                    encontrouEmail = true;
                    
                    // 3. Tenta autenticar (O método autenticar usa o Hash e o Salt)
                    if (u.autenticar(emailDigitado, senhaDigitada)) {
                        System.out.println("\n Login realizado com sucesso! Bem-vindo, " + u.getNome() + ".");
                        logado = true;
                        usuarioLogado = u; // Armazena o usuário logado
                        logado = true;
                    } else {
                        System.out.println("\n Senha incorreta. Tente novamente.\n");
                    }
                    break;
                }
            }

            if (!encontrouEmail) {
                System.out.println("\n Email não encontrado no sistema. Tente novamente.\n");
            }
        }
        
        // ===== CHAMANDO O OUTRO JAVA =====
        // Se o login deu certo, chama a classe PainelUsuario
        if (logado && usuarioLogado != null) {
            
            // admin
            if (usuarioLogado instanceof admin) {
                admin admin = (admin) usuarioLogado;
                admin.abrirMenu();
            }

            //inquilino
            else if (usuarioLogado instanceof inquilino) {
                inquilino inquilino = (inquilino) usuarioLogado;
                inquilino.abrirMenu(scanner);
            }

            //Caso der errado
            else {
                System.out.println("Tipo de usuário desconhecido. Encerrando o programa.");
            }
        }    
        scanner.close();
    }
}