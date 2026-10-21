package com.nexusmaven;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        // 1. Criando usuários (Senha com 8 caracteres!)
        Usuario u1 = new Usuario("Jorge", "jjma@hotmail.com", "12345678");
        Usuario u2 = new Usuario("admin", "admin@hotmail.com", "admin123");

        // 2. Colocando em uma lista
        List<Usuario> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(u1);
        listaUsuarios.add(u2);

        // 3. Salvando usuários no arquivo JSON
        JsonUtil.salvarUsuarios(listaUsuarios);
        System.out.println("Usuários salvos no arquivo JSON.");

        // 4. Lendo do arquivo JSON e validar o save
        System.out.println("Lendo usuários do arquivo JSON:");
        List<Usuario> usuariosLidos = JsonUtil.lerUsuarios();

        for (Usuario u : usuariosLidos) {
            System.out.println(u);
        }

        // 5. Testando autenticação
        if (!usuariosLidos.isEmpty()) {
            Usuario teste = usuariosLidos.get(0);
            boolean logado = teste.autenticar("jjma@hotmail.com", "12345678");
            System.out.println("\n Teste de login (Jorge): " + (logado ? "Autenticado com sucesso!" : "Falha na autenticação!"));
            
        }
    }
}