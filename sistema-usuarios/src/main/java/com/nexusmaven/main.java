package com.nexusmaven;

public class main {
    public static void main(String[] args) {
        Usuario u = new Usuario("Jorge", "jjma@hotmail.com", "123456");

        System.out.println(u);
        System.out.print(u.getEmail());

        // Teste de Validação
        try {
            u.setEmail("invalido");            
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println(u.autenticar("jjma@hotmail.com", "123456")); // True
        System.out.println(u.autenticar("jjma@hotmail.com", "error")); // False
    }
}