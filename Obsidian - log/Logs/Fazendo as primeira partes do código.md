Fazendo um sistema de usuário usando POO, para conversa com o json, por hora fiz o sistema this.nome as **10:52:23** estarei alogando mais o código cada fez que for evoluindo

```
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

```
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

# Missão nessa nota

- [ ] ⏫ Aprimorar o esqueleto do usuario
- [ ] ⏫ Aplicar o sistema no main