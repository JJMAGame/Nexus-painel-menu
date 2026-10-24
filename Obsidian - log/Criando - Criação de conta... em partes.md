Foi desenvolvimento um sistema chamado `GerarDados.java` e foi feito para criar novos dados, futuramente estarei melhorando esse programa para outros json talvez sobre os dados do usuários e imóvel

```java fold:
package com.nexusmaven;

  

import java.util.ArrayList;
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
        System.out.print("Digite a senha do usuário (mínimo 6 caracteres):");
        senha = scanner.nextLine();

        Usuario u = new Usuario(nome, email, senha);

        List<Usuario> lista = new ArrayList<>();
        lista.add(u);

  

        JsonUtil.salvarUsuarios(lista);
        System.out.println("✅ Usuário salvo no arquivo JSON com sucesso!");

    }
}
```