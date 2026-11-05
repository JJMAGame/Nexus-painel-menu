package com.nexusmaven;

public class Financeiro {
    private String cpfInquilino;
    private String nomeLocal;
    private double valorAluguel;
    private double agua;
    private double energia;
    private double manutencao;
    private String mesReferencia;

    // Construtor vazio para o Gson
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

    // ===== GETTERS =====
    public String getCpfInquilino() { return cpfInquilino; }
    public String getNomeLocal() { return nomeLocal; }
    public double getValorAluguel() { return valorAluguel; }
    public double getAgua() { return agua; }
    public double getEnergia() { return energia; }
    public double getManutencao() { return manutencao; }
    public String getMesReferencia() { return mesReferencia; }

    // ===== MÉTODO DE NEGÓCIO =====
    // Calcula a receita total deste aluguel (Aluguel + Contas)
    public double calcularTotal() {
        return valorAluguel + agua + energia + manutencao;
    }

    @Override
    public String toString() {
        return "Mês: " + mesReferencia + " | Local: " + nomeLocal + " | Inquilino CPF: " + cpfInquilino +
               "\n   Aluguel: R$" + valorAluguel + " | Água: R$" + agua + 
               " | Energia: R$" + energia + " | Manutenção: R$" + manutencao +
               "\n   💰 TOTAL A PAGAR: R$" + calcularTotal() + "\n";
    }
}
