public class Usuariolog {
    private String nome;
    private String email;
    private String senha;

    public Usuariolog(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    //getter

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

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
