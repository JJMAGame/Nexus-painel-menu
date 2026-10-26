2 placeholder foi criado o para dar base o que tem que ta ali naquele momento, para depois criar o produtos/fotos ou conteúdo real
![[Pasted image 20261026144111.png]]

Todos praticamente com o mesmo estado

```java fold:placeholder-admin
package com.nexusmaven;

  

public class admin extends Usuario {
    public admin(String nome, String email, String senha) {
        super(nome, email, senha);

    }
}
```

```java fold:placeholder-inquilino
package com.nexusmaven;

  

public class inquilino extends Usuario {
    public admin(String nome, String email, String senha) {
        super(nome, email, senha);

    }
}
```

Enquanto isso o main foi atualizado, para o usuário se manter logado usado o:

```java fold:Um-Novo-Main hl:24,68-69
package com.nexusmaven;

  

import java.util.List;
import java.util.Scanner;

  

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

  

        System.out.println("=================================");
        System.out.println("      TELA DE LOGIN - NEXUS      ");
        System.out.println("=================================");

  

        boolean logado = false;

        Usuario usuarioLogado = null; // Variável para armazenar o usuário logado

  

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

        // Aqui começaria o "Menu Principal" do sistema após o login

        System.out.println("\n--- Painel Principal ---");
        System.out.println("(Aqui você implementaria o resto do sistema...)");
        scanner.close(); 
    }
}
```

Bem estarei em breve melhorando o json para colocar mais informações como CPF e Imovel para outros json como:

-----------------------------
1. Nome
2. E-mail
3. Senha

4. Tipo de usuário

----------------------------------

1. Nome
2. E-mail
3. CPF
4. Imóvel
   
> [!warning] mais informações estará sendo adicionado em breve, talvez tenha mudança

------

Então ai terei que colocar os dados no banco de dados para depois o documento bruto (como exemplo)

**Herança foi incluído no inquilino e admin** 
