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


