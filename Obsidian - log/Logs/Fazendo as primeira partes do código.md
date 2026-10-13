Fazendo um sistema de usuário usando POO, para conversa com o json, por hora fiz o sistema this.nome as **10:52:23** estarei alogando mais o código cada fez que for evoluindo

```java fold:Encapsulamento
public class usuario {
    private String nome;
    private String email;
    private String senha;
    public usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
```
Após a construção dos **Strings** foi incluindo também o **Getter e Setter** do usuario

```java fold:Getter-and-Setter
`//getter`

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

  

    //setter

  

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
```
Melhorando o Obsidian para ter integração com o Git

---

Colocando o a validação de login no Usuário para criação e validação de conta
```java fold:Validação-no-Setter
    // ===== SETTERS COM VALIDAÇÃO =====

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email.toLowerCase().trim();
    }

  

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 8 caracteres");

        }
        this.senha = senha;
    }

  

    // ===== MÉTODO DE NEGÓCIO (reaproveitamento futuro) =====

    public boolean autenticar(String email, String senha) {
        return this.email.equalsIgnoreCase(email) && this.senha.equals(senha);

    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }
}

```

Ainda tenho que validar se estar tudo certo se esta no caminho certo para o encapsulamento e herança, terei que revisar o Pilares. *Quais seria os pilares?*

### **Abstração**:
Consiste em focar nos aspectos essenciais de um objeto do mundo real e representá-los no código por meio de atributos (características) e métodos (ações), ignorando detalhes complexos e desnecessários.

### **Encapsulamento**:
É a prática de esconder os detalhes internos de funcionamento de um objeto e proteger seus dados (atributos), controlando o acesso a eles por meio de métodos específicos (como _getters_ e _setters_)

### **Herança**:
Permite que uma classe (subclasse) receba ou herde atributos e comportamentos de outra classe (superclasse), facilitando a reutilização de código e a criação de hierarquias.
### **Polimorfismo**:
É a capacidade de objetos de diferentes classes responderem à mesma mensagem ou método de maneiras diferentes, adaptando o comportamento conforme o contexto de cada subclasse.
# Missão nessa nota

- [ ] ⏫ Aprimorar o esqueleto do usuario
- [ ] ⏫ Aplicar o sistema no main