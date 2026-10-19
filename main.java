public class main {
    public static void main(String[] args) {
        Usuario u = new Usuario("Jorge", "jjma@hotmail.com", "123456");

        System.out.println(u);
        System.out.print(u.getEmail());

        // Teste de Validação
        try {
            u.setEmail("invalido");            
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println(u.autenticar("jjma@hotmail.com", "123456")); // True
        System.out.println(u.autenticar("jjma@hotmail.com", "error")); // False
    }
}

class Usuario {
    private final String nome;
    private String email;
    private final String senha;

    Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.senha = senha;
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email;
    }

    public boolean autenticar(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "'}";
    }
}