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