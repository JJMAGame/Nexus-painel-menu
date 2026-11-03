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