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
