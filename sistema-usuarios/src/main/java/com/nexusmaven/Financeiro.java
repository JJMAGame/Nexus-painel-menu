package com.nexusmaven;

public class Financeiro {
    private String cpfInquilino;
    private String nomeLocal;
    private double valorAluguel;
    private double agua;
    private double energia;
    private double manutencao;
    private String mesReferencia;

    public Financeiro() {}

    public Financeiro(String cpfInquilino, String nomeLocal, double valorAluguel, double agua, double energia, double manutencao, String mesReferencia) {
        this.cpfInquilino = cpfInquilino;
        this.nomeLocal = nomeLocal;
        this.valorAluguel = valorAluguel;
        this.agua = agua;
        this.energia = energia;
        this.manutencao = manutencao;
        this.mesReferencia = mesReferencia;
    }

    public String getCpfInquilino() { return cpfInquilino; }
    public String getNomeLocal() { return nomeLocal; }
    public String getMesReferencia() { return mesReferencia; }
    
    public double calcularTotal() {
        return valorAluguel + agua + energia + manutencao;
    }

    @Override
    public String toString() {
        return "Mês: " + mesReferencia + " | Local: " + nomeLocal + " | Inquilino CPF: " + cpfInquilino +
               "\n   Aluguel: R$" + valorAluguel + " | Água: R$" + agua + 
               " | Energia: R$" + energia + " | Manutenção: R$" + manutencao +
               "\n   💰 TOTAL: R$" + calcularTotal() + "\n";
    }
}
