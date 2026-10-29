Agora que eu parei pra pensar, eu preciso fazer que o programa fique funcionando em enquanto ta em funcionamento então terei que aplicar um loop e so finalizar quando o cliente quiser que finaliza![[Pasted image 20261026194400.png]]

![[Recording 20261026212631.m4a]]
> [!quote] Então, o que está acontecendo? O código está sendo construído, está sendo muito bem feito, mas por causa desse detalhezinho que é o negócio de loopar, eu acho que eu tenho que dar prioridade a isso. Porque senão pode acontecer algumas situações que podem piorar a situação. Então, eu vou ter que priorizar muito o sistema de loop do aplicativo.
## Correção de bug

Vou percebido que o GerarDados que ele não estava respeitando as linhas de cada conta, então eu fiz alteração no código para ele ler todo o json para depois aplicar

```java fold:Fragmento-da-correção
 List<Usuario> usuarios = JsonUtil.lerUsuarios();
```

## Em relação a JsonUtil

Enquanto eu tava dando uma olhada eu percebi que o jsonUtil so estava criando um tipo de banco de dados que se chama "usuario.json"
```java fold:Fragmento-JsonUtil
public class JsonUtil {

    private static final String ARQUIVO = "usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
```

Então eu dei uma atualizada todo o código para criar novos bancos de dados ou informações

```java fold:novo-JsonUtil
package com.nexusmaven;

  

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

  

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

  

public class JsonUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  
    // <T> significa que esse método aceita qualquer classe
    public static <T> List<T> ler(String nomeArquivo, Class<T> classe) {
        File file = new File(nomeArquivo);
        if (!file.exists()) return new ArrayList<>();

  
        try (Reader reader = new FileReader(file)) {
            // Cria o tipo genérico da lista baseado na classe passada
            Type tipoLista = TypeToken.getParameterized(List.class, classe).getType();
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + nomeArquivo, e);
        }
    }

    public static <T> void salvar(String nomeArquivo, List<T> lista) {
        try (Writer writer = new FileWriter(nomeArquivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo JSON: " + nomeArquivo, e);
        }
    }
}
```

### *E as outras class?*

O `GerarDados.java` teve alteração para especificar qual banco de dados deve mexer sem ter uma informação declarado DENTRO do código

E foi criado um novo tipo de geração de dados, chamado imóvel

```java fold:GerarDados1.5
package com.nexusmaven;

  

import java.util.List;
import java.util.Scanner;

  

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha;
        System.out.println("⏳ Gerando novos dados de usuarios...");
        Scanner scanner = new Scanner(System.in);

        //Qual tipo de dados vai ser gerado:
        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               // Cria o usuário com os dados fornecidos pelo usuário
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite a senha do usuário (mínimo 6 e máximo 12 caracteres):");
           senha = scanner.nextLine();

           try{
               Usuario u = new Usuario(nome, email, senha);
               List<Usuario> usuarios = JsonUtil.ler("usuarios.json", Usuario.class);
               usuarios.add(u);
               JsonUtil.salvar("usuarios.json", usuarios);
               System.out.println("Dados de usuário gerados e salvos com sucesso!");
               System.out.println("Total de usuários no sistema: " + usuarios.size());
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }

        else if (escolha.equals("2")) {
            // Cria o imovel novo
            System.out.print("Digite o nome do imóvel: ");
            String lnome = scanner.nextLine();
            System.out.print("Digite a localização do imóvel: ");
            String localizacao = scanner.nextLine();

            try{
                Imovel imovel = new Imovel(lnome, localizacao);
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
```

Nesse meio tempo foi criado uma abstração chamado `Imovel.java` para ser molde para criação de casa ou apê (que ainda vai ser pensado).

```java fold:Imovel.java
package com.nexusmaven;

public class Imovel {

    // Dados essenciais
    private String lnome;
    private String localizacao;

    // Construtor vazio (necessário para o Gson ler o JSON)
    public Imovel() {}

    // Construtor padrão
    public Imovel(String lnome, String localizacao) {
        setNome(lnome);
        setLocalizacao(localizacao);
    }

    // ===== GETTERS =====
    public String getNome() { return lnome; }
    public String getLocalizacao() { return localizacao; }

    // ===== SETTERS COM VALIDAÇÃO =====
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do imóvel não pode ser vazio.");

        }
        this.lnome = nome.trim();
    }

    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("A localização não pode ser vazia.");
        }
        this.localizacao = localizacao.trim();
    }
}
```

# Polimorfismo

Essa parte precisei do auxilio da AI para ajudar nessa anotação, mas de forma resuma eu conseguir fazer o código `jsonUtil.Java` ler um `tipo`

`tipo = admin`
`tipo = inquilino`

e estão presentes na classe filhas, e depois disso criar um UsuarioAdapter que ele ler esse tipo e repassa para o Gson e define cada usuário **admin** ou **inquilino**

### Menção a segurança

Na ultima reunião foi mencionado que eu estou desenvolvedo a segurança do aplicativo e digo que as coisa que venho melhorando principalmente que foi o loop e ta sendo aplicado cada vez que avança

## As Armadilhas do Caminho 
(Segundo deepseek AI)

- **Construtores:** Aprendeu que o Gson precisa de um `public Usuario() {}` vazio em todas as classes para conseguir criar o objeto ao ler do JSON. Sem isso, o Java dá erro de "construtor implícito".
    
- **A Maldição do Scanner:** Você viu na prática por que um `Scanner` não deve ser um atributo (variável) de uma classe que vai para o JSON. O Gson tentou salvar o buffer do teclado inteiro e corrompeu o arquivo.
    
- **A Importância do `instanceof`:** Como o Java sabe quem abrir o menu. Ele verifica a "identidade" real do objeto em memória.
# Os Códigos atualmente
``
```java fold:Usuario-atual-29/10/2026
package com.nexusmaven;

public class Usuario {
    private String nome;
    private String email;
    private String senha;

    // Construtor vazio (Obrigatório para o Gson ler o JSON)
    public Usuario() {} 

    // Construtor reaproveita os setters (validação garantida)
    public Usuario(String nome, String email, String senha) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    // ===== GETTERS =====
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    
    // ===== SETTERS COM VALIDAÇÃO =====
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        this.email = email.trim();
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 6 || senha.length() > 12) {
            throw new IllegalArgumentException("Senha deve ter entre 6 e 12 caracteres");
        }
        this.senha = senha;
    }

    // ===== MÉTODO DE NEGÓCIO =====
    public boolean autenticar(String email, String senha) {
        return this.email.equalsIgnoreCase(email) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }   
}
```

```java fold:Admin-atual-29/10/2026
package com.nexusmaven;

import java.util.Scanner;

public class admin extends Usuario {
    private final String tipo = "admin";

    public admin() {} // Obrigatório para o Gson

    public admin(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public String getTipo() { return tipo; }

    public void abrirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("         MENU ADMIN - NEXUS        ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! Bem-vindo ao painel de administração.");
            System.out.println("1 - Gerar novos dados (Usuários/Imóveis)");
            System.out.println("2 - Registrar pagamentos");
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    GerarDados.main(null); 
                    break;
                case 2:
                    System.out.println("💰 Registrar pagamentos - funcionalidade em desenvolvimento.");
                    break;
                case 0:
                    System.out.println("👋 Saindo do menu admin...");
                    break;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }
}
```

```java fold:Inquilino-atual-29/10/2026
package com.nexusmaven;

import java.util.Scanner;

public class inquilino extends Usuario {
    private final String tipo = "inquilino";

    public inquilino() {} // Obrigatório para o Gson

    public inquilino(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public String getTipo() { return tipo; }

    public void abrirMenu(Scanner scanner) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("        MENU INQUILINO - NEXUS     ");
            System.out.println("===================================");
            System.out.println("Olá, " + getNome() + "! O que deseja fazer?");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Ver imóveis disponíveis");
            System.out.println("0 - Sair (Logout)");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    System.out.println("\n--- SEUS DADOS ---");
                    System.out.println("Nome: " + getNome());
                    System.out.println("Email: " + getEmail());
                    break;
                case 2:
                    System.out.println("\n--- IMÓVEIS DISPONÍVEIS ---");
                    // Aqui você pode ler o imoveis.json e listar
                    System.out.println("(Listagem de imóveis em desenvolvimento...)");
                    break;
                case 0:
                    System.out.println("👋 Saindo do menu do inquilino...");
                    break;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }
}
```

```java fold:imovel-atual-29/10/2026
package com.nexusmaven;

public class Imovel {

    // Dados essenciais
    private String lnome;
    private String localizacao;

    // Construtor vazio (necessário para o Gson ler o JSON)
    public Imovel() {}

    // Construtor padrão
    public Imovel(String lnome, String localizacao) {
        setNome(lnome);
        setLocalizacao(localizacao);
    }

    // ===== GETTERS =====
    public String getNome() { return lnome; }
    public String getLocalizacao() { return localizacao; }

    // ===== SETTERS COM VALIDAÇÃO =====
    public void setNome(String lnome) {
        if (lnome == null || lnome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do imóvel não pode ser vazio.");
        }
        this.lnome = lnome.trim();
    }

    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("A localização não pode ser vazia.");
        }
        this.localizacao = localizacao.trim();
    }
}
```

```java fold:JsonUtil-Remodelado-29/10/2026
package com.nexusmaven;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Usuario.class, new UsuarioAdapter())
        .create();

    // <T> significa que esse método aceita qualquer classe
    public static <T> List<T> ler(String nomeArquivo, Class<T> classe) {
        File file = new File(nomeArquivo);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            // Cria o tipo genérico da lista baseado na classe passada
            Type tipoLista = TypeToken.getParameterized(List.class, classe).getType();
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + nomeArquivo, e);
        }
    }

    public static <T> void salvar(String nomeArquivo, List<T> lista) {
        try (Writer writer = new FileWriter(nomeArquivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo JSON: " + nomeArquivo, e);
        }
    }

    // =========================================================
    // O TRADUTOR (Lê a etiqueta "tipo" e cria a classe correta)
    // =========================================================
    private static class UsuarioAdapter implements JsonDeserializer<Usuario> {
        @Override
        public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            
            // Verifica se a etiqueta "tipo" existe no JSON
            if (jsonObject.has("tipo")) {
                String tipo = jsonObject.get("tipo").getAsString();
                if (tipo.equals("admin")) {
                    return context.deserialize(jsonObject, admin.class);
                } else if (tipo.equals("inquilino")) {
                    return context.deserialize(jsonObject, inquilino.class);
                }
            }
            
            // Se não tiver a etiqueta "tipo", tenta ler como um Usuario comum
            return context.deserialize(jsonObject, Usuario.class);
        }
    }
}
```

```java fold:GerarDados-Atualizado-29/10/2026
package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha;
        System.out.println("⏳ Gerando novos dados de usuarios...");
        Scanner scanner = new Scanner(System.in);

        //Qual tipo de dados vai ser gerado:
        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               // Cria o usuário com os dados fornecidos pelo usuário
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite a senha do usuário (mínimo 6 e máximo 12 caracteres): ");
               senha = scanner.nextLine();

               //Qual é o tipo?
               System.out.println("\nQual o tipo de conta?");
               System.out.println("1 - Inquilino (Padrão)");
               System.out.println("2 - Administrador");
               System.out.print("Escolha o tipo: ");
               String tipoConta = scanner.nextLine();

           try{
               Usuario u = null;

               if (tipoConta.equals("2")) {
                  u = new admin(nome, email, senha);
               } else {
                  u = new inquilino(nome, email, senha);
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
            // Cria o imovel novo
            System.out.print("Digite o nome do imóvel: ");
            String lnome = scanner.nextLine();
            System.out.print("Digite a localização do imóvel: ");
            String localizacao = scanner.nextLine();

            try{
                Imovel imovel = new Imovel(lnome, localizacao);
   
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
```

```java fold:main-atualizado-29/10/2026
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
                    
                    // 3. Tenta autenticar
                    if (u.autenticar(emailDigitado, senhaDigitada)) {
                        System.out.println("\n Login realizado com sucesso! Bem-vindo, " + u.getNome() + ".");
                        logado = true;
                        usuarioLogado = u; // Armazena o usuário logado
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
        
        // ===== CHAMANDO O OUTRO JAVA (POLIMORFISMO) =====
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
```

### Menção a ferramentas importantes:

**POM** com o a dependência Gson

```xml fold:POM-Atual
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.nexusmaven</groupId>
  <artifactId>sistema-usuarios</artifactId>
  <version>1.0-SNAPSHOT</version>

  <name>sistema-usuarios</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.release>25</maven.compiler.release>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.10.1</version>
    </dependency>
  </dependencies>

  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.16.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.7.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.0.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```

Um dos arquivos gerado pelo sistema
```json fold:Teste-atual
[
  {
    "tipo": "admin",
    "nome": "admin",
    "email": "admin@admin.com",
    "senha": "admin123"
  },
  {
    "tipo": "inquilino",
    "nome": "comum",
    "email": "comum@comum.com",
    "senha": "comum123"
  }
]
```

## Fluxo atual do sistema

![[Pasted image 20261029181917.png]]

# O que falta?

Construção da telas do **Inquilino**, **Adminstrador** e **Secretaria** com os privilégios de cada um deles sendo o máximo o Administrador