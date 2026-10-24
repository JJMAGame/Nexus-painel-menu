Foi desenvolvimento um sistema chamado `GerarDados.java` e foi feito para criar novos dados, futuramente estarei melhorando esse programa para outros json talvez sobre os dados do usuários e imóvel

```java fold:GerarDados.Java hl:27-33
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

com tudo eu testei o programa e conseguir criar uma conta chamada *Carlos* e um novo banco de dados.

> [!quote] que alias eu lembrei no momento que to anotado que eu estou aplicando o CRUD

Aqui estão os dados do Json

```json fold:usuario hl:3-5
[
  {
    "nome": "Carlos",
    "email": "carlos@hotmail.com",
    "senha": "123456"
  }
]
```

## e finalmente o verdadeiro main

O main finalmente foi feito com o sistema de login usando o scanner e um sistema de leitura json para a validação da existência da conta

```java fold:main-verdadeiro
package com.nexusmaven;

  

import java.util.List;
import java.util.Scanner;

  

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

  

        System.out.println("=================================");
        System.out.println("      TELA DE LOGIN - NEXUS      ");
        System.out.println("=================================");

  

        boolean logado =false;

  

        //loop de login
        while (!logado) {
            System.out.print("Email: ");
            String emailDigitado = scanner.nextLine();

  
            System.out.print("Senha: ");
            String senhaDigitada = scanner.nextLine();

            List<Usuario> usuarios = JsonUtil.lerUsuarios();

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

        // Aqui começaria o "Menu Principal" do sistema após o login

        System.out.println("\n--- Painel Principal ---");
        System.out.println("(Aqui você implementaria o resto do sistema...)");
        scanner.close();

    }
}
```

O aplicativo em si esta sendo construído e o login esta funcional, falta dividir os menus do inquilino e classificar cada conta em banco de dados diferentes