package com.nexusmaven;

import java.util.Locale;
import java.util.regex.Pattern;

public class Usuario {
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private String nome;
    private String email;
    private String senha;
    private String cpf;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String cpf) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setCpf(cpf);
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCpf() { return cpf; }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome não pode ser vazio");
        this.nome = nome.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email não pode ser vazio");
        String normalizado = email.trim().toLowerCase(Locale.ROOT);
        if (!PADRAO_EMAIL.matcher(normalizado).matches()) throw new IllegalArgumentException("Email inválido");
        this.email = normalizado;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 6 || senha.length() > 12) throw new IllegalArgumentException("Senha deve ter entre 6 e 12 caracteres");
        this.senha = senha;
    }

    public void setCpf(String cpf) { // ⚠️ RECOLOCADO
        if (cpf == null || cpf.trim().isEmpty()) throw new IllegalArgumentException("CPF não pode ser vazio");
        this.cpf = cpf.trim();
    }

    public boolean autenticar(String email, String senha) {
        return email != null && email.equalsIgnoreCase(this.email) && senha != null && senha.equals(this.senha);
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "', cpf='" + cpf + "'}";
    }
}
