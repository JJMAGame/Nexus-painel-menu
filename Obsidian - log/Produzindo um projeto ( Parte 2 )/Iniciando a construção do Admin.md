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