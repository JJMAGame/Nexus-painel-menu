package com.nexusmaven;

public class Usuario {
    private String nome;
    private String email;
    private String senha;

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
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres");
        }
        this.senha = senha;
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equalsIgnoreCase(email) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }   
}
