package com.nexusmaven;

import com.google.gson.annotations.SerializedName;

public class Imovel {
    @SerializedName(value = "nome", alternate = {"lnome"})
    private String nome;
    private String localizacao;
    private String cpfProprietario; // ⚠️ RECOLOCADO
    private double valorAluguel;    // ⚠️ RECOLOCADO

    public Imovel() {}

    public Imovel(String nome, String localizacao, String cpfProprietario, double valorAluguel) {
        setNome(nome);
        setLocalizacao(localizacao);
        setCpfProprietario(cpfProprietario);
        setValorAluguel(valorAluguel);
    }

    public String getNome() { return nome; }
    public String getLocalizacao() { return localizacao; }
    public String getCpfProprietario() { return cpfProprietario; }
    public double getValorAluguel() { return valorAluguel; }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("O nome do imóvel não pode ser vazio.");
        this.nome = nome.trim();
    }

    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) throw new IllegalArgumentException("A localização não pode ser vazia.");
        this.localizacao = localizacao.trim();
    }

    public void setCpfProprietario(String cpfProprietario) {
        if (cpfProprietario == null || cpfProprietario.trim().isEmpty()) throw new IllegalArgumentException("O CPF do proprietário não pode ser vazio.");
        this.cpfProprietario = cpfProprietario.trim();
    }

    public void setValorAluguel(double valorAluguel) {
        if (valorAluguel < 0) throw new IllegalArgumentException("O valor do aluguel não pode ser negativo.");
        this.valorAluguel = valorAluguel;
    }
}