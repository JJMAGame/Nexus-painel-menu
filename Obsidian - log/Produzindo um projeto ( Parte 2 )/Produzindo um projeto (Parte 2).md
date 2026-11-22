# Produzindo um projeto (Parte 2)

**Compilação das anotações da Parte 2** — ordem: Iniciando a construção do Admin → Percepção → Direcionamento (com atualizações) → Roteiro de apresentação.

## Sumário
- [[#1. Iniciando a construção do Admin]]
- [[#2. Percepção]]
- [[#3. Direcionamento]]
- [[#4. Roteiro de apresentação — Nexus (5 min)]]

---

# 1. Iniciando a construção do Admin
estive no hiato de 3 dias (porque eu fiquei doente), mas retornei e com a construção e atualização do fluxograma.
![[Pasted image 20261103090957.png]]
Após a construção da parte administrativa, percebi que é mais seguro colocar algumas funções administrativas apenas apenas para administrativas e a outra parte ser acessível para secretaria

## mas ainda tenho que melhorar o banco de dados que ainda ta em falta

Então o que foi colocado, o **usuário** agora tem o CPF, mas ainda estarei colocando mais coisas

```java fold:usuario-com-cpf hl:9,53-58
package com.nexusmaven;

  

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String cpf;

  

    // ⚠️ ADICIONE ESTE CONSTRUTOR VAZIO AQUI
    public Usuario() {}

    // Construtor reaproveita os setters (validação garantida)
    public Usuario(String nome, String email, String senha, String cpf) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setCpf(cpf);
    } 

    // ===== GETTERS =====
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCpf() { return cpf; }

    // O Gson precisa do getter para salvar a senha no arquivo

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

    public void setCpf(String cpf) { // ⚠️ NOVO
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }
    this.cpf = cpf.trim();
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equalsIgnoreCase(email) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }  
}
```

Após isso alguns códigos foram atualizados para adicionar a nova herança, além da nova geração dos dados

**Inquilino e imóvel**

```java fold:inquilino-atualizado hl:6-7,15,16
package com.nexusmaven;
 
import java.util.Scanner;

public class inquilino extends Usuario {
    private final String tipo = "inquilino";

  

    public inquilino() {} // Obrigatório para o Gson

  

    public inquilino(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
    }

    public String getTipo() { return tipo; }
        public void abrirMenu(Scanner scanner) {
        int opcao = -1;

  

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("        MENU INQUILINO - NEXUS     ");
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

```java fold:admin-atualizado hl:13-14
package com.nexusmaven;

import java.util.Scanner;

public class admin extends Usuario {
    private final String tipo = "admin";

  

    public admin() {} // Obrigatório para o Gson

  
    public admin(String nome, String email, String senha, String cpf) {
        super(nome, email, senha, cpf);
    }

  

    public String getTipo() { return tipo; }

  

        public void abrirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

  

        while (opcao != 0) {
            System.out.println("\n=================================");
            System.out.println("         MENU ADMIN - NEXUS        ");
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
                    // Chama a classe que gera dados (que também tem o seu próprio Scanner)
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

E tudo que foi para auxiliar no **GerarDados**

```java fold:GerarDados-Atualizado hl:23-24,37-41,62-63
package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha, cpf;
        System.out.println("⏳ Gerando novos dados de usuarios...");
        Scanner scanner = new Scanner(System.in);

        //Qual tipo de dados vai ser gerado:
        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite o CPF do usuário: "); // ⚠️ NOVO
               cpf = scanner.nextLine();                      // ⚠️ NOVO
               System.out.print("Digite a senha do usuário (mínimo 6 e máximo 12 caracteres):");
               senha = scanner.nextLine();

               System.out.println("\nQual o tipo de conta?");
               System.out.println("1 - Inquilino (Padrão)");
               System.out.println("2 - Administrador");
               System.out.print("Escolha o tipo: ");
               String tipoConta = scanner.nextLine();

           try{
               Usuario u = null;

               if (tipoConta.equals("2")) {
                  u = new admin(nome, email, senha, cpf);     // ⚠️ Passando o CPF
               } else {
                  u = new inquilino(nome, email, senha, cpf); // ⚠️ Passando o CPF
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
            System.out.print("Digite o nome do imóvel: ");
            String lnome = scanner.nextLine();
            System.out.print("Digite a localização do imóvel: ");
            String localizacao = scanner.nextLine();
            System.out.print("Digite o CPF do proprietário: "); // ⚠️ NOVO
            String cpfProprietario = scanner.nextLine();        // ⚠️ NOVO

            try{
                // Passando o CPF para o Imóvel
                Imovel imovel = new Imovel(lnome, localizacao, cpfProprietario);
   
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

Mas ainda falta muita coisa para o desenvolvimento do menu do painel administrativo que é melhor já iniciar para depois se preocupar com loop.

### Uma atualização que o Copilot fez

*Mensagem do copilot*

Eu verifiquei o projeto e vi que ele **já estava configurado para Java 25**: o `pom.xml` define o release 25, e o README e um teste também verificam essa versão. Por isso, não alterei os arquivos do projeto.

O que fiz foi preparar o ambiente local:

- Instalei o **Eclipse Temurin JDK 25.0.5 LTS** em `C:\Users\jorgi\.jdk\jdk-25.0.5+8`.
- Configurei a variável de usuário `JAVA_HOME` para apontar para esse JDK.
- Instalei o **Maven 3.9.15** em `C:\Users\jorgi\.maven\apache-maven-3.9.15`.
- Executei a compilação e os testes com Java 25: **os 2 testes passaram**.

Um detalhe: o comando `java` ainda pode iniciar o Java 27, porque ele continua antes do Java 25 no `PATH` do sistema. Não alterei esse `PATH`; o Maven usa o Java 25 por meio de `JAVA_HOME`.

---
# Módulo Financeiro e Codificação UTF-8

## 1. Adicionando Valor de Aluguel ao Imóvel

Para que o sistema saiba quanto custa o aluguel, adicionamos o atributo `valorAluguel` na classe `Imovel`. Usamos o tipo `double` (ponto flutuante) para lidar com valores monetários.


```java fold:imoveis-atualizado
package com.nexusmaven;

public class Imovel {

    // Dados essenciais
    private String lnome;
    private String localizacao;
    private String cpfProprietario;
    private double valorAluguel;

    // Construtor vazio (necessário para o Gson ler o JSON)
    public Imovel() {}

    // Construtor padrão atualizado
    public Imovel(String lnome, String localizacao, String cpfProprietario, double valorAluguel) {
        setNome(lnome);
        setLocalizacao(localizacao);
        setCpfProprietario(cpfProprietario);
        setValorAluguel(valorAluguel);
    }

    // ===== GETTERS =====
    public String getNome() { return lnome; }
    public String getLocalizacao() { return localizacao; }
    public String getCpfProprietario() { return cpfProprietario; }
    public double getValorAluguel() { return valorAluguel; }

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

    public void setCpfProprietario(String cpfProprietario) {
        if (cpfProprietario == null || cpfProprietario.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do proprietário não pode ser vazio.");
        }
        this.cpfProprietario = cpfProprietario.trim();
    }

    public void setValorAluguel(double valorAluguel) { 
        if (valorAluguel < 0) {
            throw new IllegalArgumentException("O valor do aluguel não pode ser negativo.");
        }
        this.valorAluguel = valorAluguel;
    }

    @Override
    public String toString() {
        return "Imóvel [Nome: " + lnome + ", Local: " + localizacao + ", Aluguel: R$" + valorAluguel + "]";
    }
}
```

## 2. Criando o Módulo Financeiro

Para registrar os pagamentos mensais (Aluguel + Água + Energia + Manutenção), criamos uma nova classe chamada `Financeiro`. Ela será salva em um arquivo separado (`financeiro.json`).

```java fold:Financeiro
package com.nexusmaven;

public class Financeiro {
    private String cpfInquilino;
    private String nomeLocal;
    private double valorAluguel;
    private double agua;
    private double energia;
    private double manutencao;
    private String mesReferencia;

    // Construtor vazio para o Gson
    public Financeiro() {}

    public Financeiro(String cpfInquilino, String nomeLocal, double valorAluguel, double agua, double energia, double manutencao, String mesReferencia) {
        this.cpfInquilino = cpfInquilino;
        this.nomeLocal = nomeLocal;
        this.valorAluguel = valorAluguel;
        this.agua = agua;
        this.energia = energia;
        this.manutencao = manutencao;
        this.mesReferencia = mesReferencia;
    }

    // ===== GETTERS =====
    public String getCpfInquilino() { return cpfInquilino; }
    public String getNomeLocal() { return nomeLocal; }
    public double getValorAluguel() { return valorAluguel; }
    public double getAgua() { return agua; }
    public double getEnergia() { return energia; }
    public double getManutencao() { return manutencao; }
    public String getMesReferencia() { return mesReferencia; }

    // ===== MÉTODO DE NEGÓCIO =====
    // Calcula a receita total deste aluguel (Aluguel + Contas)
    public double calcularTotal() {
        return valorAluguel + agua + energia + manutencao;
    }

    @Override
    public String toString() {
        return "Mês: " + mesReferencia + " | Local: " + nomeLocal + " | Inquilino CPF: " + cpfInquilino +
               "\n   Aluguel: R$" + valorAluguel + " | Água: R$" + agua + 
               " | Energia: R$" + energia + " | Manutenção: R$" + manutencao +
               "\n   💰 TOTAL A PAGAR: R$" + calcularTotal() + "\n";
    }
}
```

Depois disso o `GerarDados.java` foi atualizado para ter agora uma 3 opção

```java fold:GerarDados-Atualizado
package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {
    public static void main(String[] args) {
        String nome, email, senha, cpf;
        System.out.println("⏳ Gerando novos dados...");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imovel");
        String escolha = scanner.nextLine();

        if (escolha.equals("1")) {
               System.out.print("Digite o nome do usuário: ");
               nome = scanner.nextLine();
               System.out.print("Digite o email do usuário: ");
               email = scanner.nextLine();
               System.out.print("Digite o CPF do usuário: "); 
               cpf = scanner.nextLine();                      
               System.out.print("Digite a senha (mínimo 6 e máximo 12 caracteres): ");
               senha = scanner.nextLine();

               System.out.println("\nQual o tipo de conta?");
               System.out.println("1 - Inquilino (Padrão)");
               System.out.println("2 - Administrador");
               System.out.print("Escolha o tipo: ");
               String tipoConta = scanner.nextLine();

           try{
               Usuario u = null;

               if (tipoConta.equals("2")) {
                  u = new admin(nome, email, senha, cpf);     
               } else {
                  u = new inquilino(nome, email, senha, cpf); 
               }
               
               List<Usuario> usuarios = JsonUtil.ler("usuarios.json", Usuario.class);
               usuarios.add(u);
   
               JsonUtil.salvar("usuarios.json", usuarios);
   
               String tipoExibicao = tipoConta.equals("2") ? "Administrador" : "Inquilino";
               System.out.println("✅ Usuário do tipo " + tipoExibicao + " salvo com sucesso!");
               System.out.println("Total de usuários no sistema: " + usuarios.size());
   
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }
        
        else if (escolha.equals("2")) {
            System.out.print("Digite o nome do imóvel: ");
            String lnome = scanner.nextLine();
            System.out.print("Digite a localização do imóvel: ");
            String localizacao = scanner.nextLine();
            System.out.print("Digite o CPF do proprietário: "); 
            String cpfProprietario = scanner.nextLine();        

            // ⚠️ NOVO: Pedindo o valor do aluguel
            System.out.print("Digite o valor do aluguel (ex: 1500.50): ");
            double valorAluguel = 0;
            try {
                valorAluguel = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Erro: Valor do aluguel inválido. Use ponto para centavos.");
                return; // Cancela o cadastro
            }

            try{
                // Passando o valor para o Imóvel
                Imovel imovel = new Imovel(lnome, localizacao, cpfProprietario, valorAluguel);
   
               List<Imovel> imoveis = JsonUtil.ler("imoveis.json", Imovel.class);
               imoveis.add(imovel);
   
               JsonUtil.salvar("imoveis.json", imoveis);
   
               System.out.println("✅ Imóvel salvo com sucesso! Aluguel: R$" + valorAluguel);
               System.out.println("Total de imóveis no sistema: " + imoveis.size());
   
           } catch (IllegalArgumentException e) {
               System.out.println("❌ Erro: " + e.getMessage());
           }
        }
            else if (escolha.equals("3")) {
            System.out.println("\n--- REGISTRO FINANCEIRO ---");
            System.out.print("Digite o CPF do Inquilino: ");
            String cpfInquilino = scanner.nextLine();
            
            System.out.print("Digite o nome do Local (Imóvel): ");
            String nomeLocal = scanner.nextLine();

            System.out.print("Digite o mês de referência (ex: 10/2026): ");
            String mes = scanner.nextLine();

            try {
                System.out.print("Valor do Aluguel: R$ ");
                double aluguel = Double.parseDouble(scanner.nextLine());
                
                System.out.print("Valor da Água: R$ ");
                double agua = Double.parseDouble(scanner.nextLine());
                
                System.out.print("Valor da Energia: R$ ");
                double energia = Double.parseDouble(scanner.nextLine());
                
                System.out.print("Valor da Manutenção: R$ ");
                double manutencao = Double.parseDouble(scanner.nextLine());

                Financeiro fin = new Financeiro(cpfInquilino, nomeLocal, aluguel, agua, energia, manutencao, mes);

                List<Financeiro> listaFin = JsonUtil.ler("financeiro.json", Financeiro.class);
                listaFin.add(fin);
                JsonUtil.salvar("financeiro.json", listaFin);

                System.out.println("\n✅ Registro financeiro salvo com sucesso!");
                System.out.println("Total gerado neste mês: R$ " + fin.calcularTotal());

            } catch (NumberFormatException e) {
                System.out.println("❌ Erro: Digite apenas números para os valores (use ponto para centavos).");
            }
        }
    }
  }
```

##  3. O Problema do UTF-8 (Acentos e Interrogações)

>[!warning] isso ainda não foi validado como corrigido

Ao gerar o JSON, notamos que palavras como "São" e "Paraíso" estavam virando `S?o` e `Para?so`.

**Causa:** O Windows/PowerShell não estava configurado para UTF-8, e o Gson estava tentando escapar caracteres especiais (HTML Escaping).

---
# Inquilino atualizado

nesse meio tempo o inquilino foi construído junto com todo código.
o código foi completamente inovado, mas ouve uma serie de bug que tive que corrigir para fazer o processo de importação do projeto, mas de resto ta bom

---

# 2. Percepção

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
(Segundo deepseek AI com as anotações minhas)

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

---

# 3. Direcionamento

Essa anotação vai ser dedicado ao direcionamento de cada usuario e o que serão exibido

informação do admin

tipo:
nome:
email:
senha:
cpf:

informação do secretaria

tipo:
nome:
email:
senha:
cpf:

informação do inquilino

tipo:
nome:
email:
senha:
cpf:

informação do imovel

*tipoImovel*:
- casa
- apartamento
- Quarpão
localização
inquilino:(nome, cpf)
fiador
Conta de luz
Conta de agua
aluguel

---------------------






## 3.1 Atualizações desta anotação (o que mudou)

> [!note] Contexto
> Esta seção complementa o Direcionamento original. Ele foi escrito antes da implementação dos módulos de cobranças, despesas e locações. Abaixo, o que mudou no projeto e o que ainda falta.

### O que já foi implementado

- **Perfis:** hoje existem apenas `admin` e `inquilino`. O perfil **secretaria** planejado aqui ainda não foi implementado; na prática, o `admin` acumula as funções administrativas e as de secretaria.
- **Usuário:** o `cpf` entrou de fato no modelo (`Usuario.java`), com validação de 11 dígitos; `nome`, `email` (normalizado e validado por regex) e `senha` (6–12) também são validados. O campo `tipo` ("admin"/"inquilino") é o discriminador usado pelo Gson.
- **Imóvel:** o modelo final tem `nome`, `localizacao`, `cpfProprietario` e `valorAluguel`. O `lnome` antigo foi normalizado para `nome` (com `@SerializedName` aceitando o formato antigo).
- **Contas de luz/água:** deixaram de ser campos do imóvel. Viraram **Despesas da Fundação** (`Despesa`, `CategoriaDespesa`, `StatusDespesa`), com fluxo Aguardando aprovação → A pagar → Pago, gravadas em `despesas.json`.
- **Aluguel:** virou **Cobrança** (`Cobranca`, que herda de `Lancamento`), com mês de referência, vencimento, multa de 10% e juros de 1% ao mês; gravada em `cobrancas.json`.
- **Financeiro:** a classe `Financeiro`/`financeiro.json` foi descartada e substituída por `Cobranca` (receitas) + `Despesa` (despesas), ambas filhas de `Lancamento`.
- **Locação:** o processo ganhou `ProcessoLocacao`, `EtapaLocacao` (Proposta → Documentos → Consulta CPF → Renda → Aprovação → Vistoria → Contrato → Chaves → Concluída), `LocacaoService` e `MenuLocacoes`, com regra de renda mínima de 3× o aluguel e pendências por etapa.
- **Inquilino:** menu com 9 opções (dados, imóveis, faturas, locação, pagar, recibos, documentos, avisos, contato), sempre filtrado pelo CPF do próprio inquilino.
- **Status da cobrança:** regra de 7 dias — **Em dia** (pago ou faltando mais de 7 dias), **A vencer** (7 dias ou menos) e **Atrasado** (após o vencimento, com multa e juros).
- **Persistência:** cinco arquivos JSON (`usuarios.json`, `imoveis.json`, `cobrancas.json`, `despesas.json`, `locacoes.json`), gravação atômica e UTF-8 estrito.

### O que ainda falta do direcionamento original

- Perfil **secretaria** com privilégios próprios (hoje só admin e inquilino).
- `tipoImovel` (casa, apartamento, quarpão) no cadastro do imóvel.
- Campos de **fiador** e de **contrato** vinculados ao imóvel/locação.
- Contas de luz/água **por imóvel** (hoje são despesas gerais da Fundação).
- Dados bancários do inquilino e upload/controle de documentos (hoje o menu só orienta a entregar na Secretaria).

### Comparativo rápido

| Planejado no Direcionamento | Estado atual |
|---|---|
| admin / secretaria / inquilino | admin / inquilino (secretaria pendente) |
| cpf no usuário | ✅ implementado (11 dígitos) |
| imóvel: tipo, fiador, contas, aluguel | nome, localização, cpfProprietario, valorAluguel |
| financeiro por imóvel | Cobrança (aluguel) + Despesa (contas) |
| status/pagamento | cobrança com 7 dias + multa/juros; despesa com aprovação |
| locação | 9 etapas + pendências + regra 3× |

---

# 4. Roteiro de apresentação — Nexus (5 min)

**Duração:** 5 minutos · **Formato:** Login (main) → Inquilino → Admin
**Foco:** mostrar, grupo a grupo, quais conceitos de POO foram respeitados
**Stack:** Java 21, Maven, Gson, JUnit · **Autor:** Melqui

> [!tip] Regra de ouro
> O código é o slide. Em cada grupo, mostre **1 arquivo** e diga **1 conceito respeitado**. Não explique o menu inteiro.

## Cronômetro

| Tempo | Grupo | O que mostrar |
|---|---|---|
| 0:00–0:15 | Abertura | 1 frase sobre o sistema |
| 0:15–1:25 | **Login (main) + Usuario** | `main.java`, `Usuario.java` |
| 1:25–2:25 | **Inquilino** | `inquilino.java`, `Cobranca.getSituacao()` |
| 2:25–4:30 | **Admin** | `admin.java`, `Lancamento`/`Cobranca`/`Despesa`, `RegraDeEncargos` |
| 4:30–4:45 | Extras | tabela rápida |
| 4:45–5:00 | Fechamento | resumo dos 4 pilares |

---

## 0:00–0:15 — Abertura

> [!tip] Fala
> "O **Nexus** é um sistema de gestão de aluguéis em **Java 21 + Maven + Gson**. Vou percorrer o fluxo **Login → Inquilino → Admin** e, em cada etapa, mostrar quais conceitos de POO foram respeitados."

---

## 0:15–1:25 — Grupo 1: Login (`main.java`) + `Usuario.java`

**Abrir:** `main.java` e `Usuario.java`.

> [!tip] Fala
> "O `main` faz o login e decide o que abrir. Repare que ele **não** mexe em banco nem em menu: ele só conversa com `Usuario`."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Abstração** | O login trabalha com `Usuario`, o tipo comum — sem saber ainda se é admin ou inquilino. |
| **Encapsulamento** | Em `Usuario`: campos `private`; setters validam nome, e-mail (regex), senha (6–12) e CPF (11 dígitos); `equals`/`hashCode` por CPF. |
| **Polimorfismo** | `main` decide em execução com `instanceof admin admin` / `instanceof inquilino inquilino`; o `JsonUtil.UsuarioAdapter` reconstrói a subclasse pelo campo `tipo`. |
| **Herança (base)** | `admin` e `inquilino` **estendem** `Usuario` — a hierarquia que os próximos grupos usam. |

> [!important] Ponto para destacar
> `Usuario` já entrega de graça o **encapsulamento** (ninguém deixa o objeto inválido) e é a **base do polimorfismo** (o `main` e o `JsonUtil` tratam tipos diferentes pelo mesmo tipo `Usuario`).

---

## 1:25–2:25 — Grupo 2: Inquilino (`inquilino.java`)

**Abrir:** `inquilino.java` e `Cobranca.getSituacao()`.

> [!tip] Fala
> "O inquilino não reimplementa nada de usuário: ele **herda** nome, e-mail, CPF e `autenticar`. E só enxerga o que é dele."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Herança** | `inquilino extends Usuario` — reaproveita identidade e `autenticar`. |
| **Encapsulamento** | As consultas filtram por `getCpf()`; o menu só lê pelos getters. |
| **Abstração** | Usa `Cobranca`/`ProcessoLocacao`/`EtapaLocacao` sem conhecer os detalhes internos. |
| **Polimorfismo** | `Cobranca.getSituacao()` é a sobrescrita de `Lancamento.getSituacao()` — reage ao estado (Em dia / A vencer / Atrasado). |

---

## 2:25–4:30 — Grupo 3: Admin (`admin.java` + menus + domínio)

**Abrir:** `admin.java`, `Lancamento.java`, `Cobranca.java`/`Despesa.java`, `RegraDeEncargos.java`/`EncargosContratuais.java`.

> [!tip] Fala
> "O admin herda de Usuario e delega cada área para um menu. No domínio, a POO aparece com força: `Lancamento` abstrata, `Cobranca`/`Despesa` especializadas e a regra de encargos plugável."

**O que foi respeitado aqui:**

| Conceito | Onde aparece |
|---|---|
| **Herança** | `admin extends Usuario`; `Cobranca`/`Despesa extends Lancamento` (herdam id, valor, vencimento, pagamento). |
| **Polimorfismo** | `getDescricao()`/`getSituacao()` sobrescritos; `RegraDeEncargos` injetada em `Cobranca.getMulta(...)` (Strategy). |
| **Abstração** | `Lancamento` abstrata e `RegraDeEncargos` interface. |
| **Encapsulamento** | `setValor` rejeita ≤ 0; `marcarComoPago` é `protected`. |
| **Composição** | `MenuLocacoes` tem `LocacaoService`; `MenuCobrancas` tem `RegraDeEncargos`. |

> [!important] Frase de efeito
> "Trocar a multa é criar outra `RegraDeEncargos` e injetar — `Cobranca` não muda."

---

## 4:30–4:45 — Extras (só se sobrar tempo)

| Conceito | Onde |
|---|---|
| Construtor padrão/parametrizado | `Usuario()`, `Cobranca(...)` |
| `this` e `super` | `admin`/`inquilino`, `Cobranca`/`Despesa` |
| `static`/`final` | `JsonUtil.ARQUIVO_USUARIOS`, `EncargosContratuais.PERCENTUAL_MULTA` |
| Interface + `default` | `RegraDeEncargos.totalComEncargos` |
| Enum com métodos | `EtapaLocacao` (`proxima`, `anterior`, `pendenciaPadrao`) |
| Generics / streams | `<T> List<T> ler(...)`, `stream().mapToInt().max()` |
| Exceções | `IllegalArgumentException`, `IllegalStateException` |

---

## 4:45–5:00 — Fechamento

> [!tip] Fala
> "Resumindo: **Login** mostrou encapsulamento e a base do polimorfismo em `Usuario`; **Inquilino** mostrou herança e abstração no uso; **Admin** fechou com herança, polimorfismo e abstração no domínio. Obrigado — perguntas?"

---

## Cola de demo (opcional, 30 s)

- Admin: `admin@admin.com` / `admin123` → **6 – Processos de Locação** → **2 – Ver painel**.
- Inquilino: `jorgealves@hotmail.com` / `jorge2004` → **3 – Minhas faturas** (cobrança 12/2026 "Em dia").

> [!warning] Não crie dados novos durante a apresentação
> Se for demonstrar criação, use uma cópia dos `.json`.

## Como rodar

```shell
mvn -f sistema-usuarios clean package
java -cp sistema-usuarios/target/classes com.nexusmaven.main
```

## Perguntas prováveis

1. **Onde está o polimorfismo?** → `getDescricao/getSituacao` + `RegraDeEncargos` injetada + `instanceof` no `main`.
2. **Por que herança em admin/inquilino?** → ambos são usuários e compartilham `autenticar`.
3. **Por que JSON?** → simplicidade; persistência isolada em `JsonUtil`.
4. **Senha protegida?** → não (didático); evolução: hash + salt.

---

#nexus #java #poo #apresentacao


---

#nexus #java #poo #apresentacao #logs