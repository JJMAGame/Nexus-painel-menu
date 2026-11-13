package com.nexusmaven;

import java.util.ArrayList;
import java.util.List;

public class ProcessoLocacao {
    private String id; // Ex: "LOC-001"
    private String cpfInquilino; // Aponta para o Usuario
    private String nomeImovel;   // Aponta para o Imovel
    private String etapaAtual;   // Guarda o nome do Enum (ex: "DOCUMENTOS")
    private List<String> pendencias;
    private double rendaMensal;  // Para a regra da imagem (Renda 3x)

    public ProcessoLocacao() {
        this.pendencias = new ArrayList<>();
    }

    public ProcessoLocacao(String id, String cpfInquilino, String nomeImovel, EtapaLocacao etapaInicial, double rendaMensal) {
        this.id = id;
        this.cpfInquilino = cpfInquilino;
        this.nomeImovel = nomeImovel;
        this.etapaAtual = etapaInicial.name();
        this.rendaMensal = rendaMensal;
        this.pendencias = new ArrayList<>();
    }

    // Getters e Setters necessários para o Gson e para o Menu
    public String getId() { return id; }
    public String getCpfInquilino() { return cpfInquilino; }
    public String getNomeImovel() { return nomeImovel; }
    public double getRendaMensal() { return rendaMensal; }

    public EtapaLocacao getEtapaAtual() {
        return EtapaLocacao.valueOf(this.etapaAtual);
    }

    public void setEtapaAtual(EtapaLocacao etapa) {
        this.etapaAtual = etapa.name();
    }

    public List<String> getPendencias() {
        if (pendencias == null) pendencias = new ArrayList<>();
        return pendencias;
    }

    public void adicionarPendencia(String pendencia) {
        if (!getPendencias().contains(pendencia)) {
            getPendencias().add(pendencia);
        }
    }

    public void resolverPendencia(String pendencia) {
        getPendencias().remove(pendencia);
    }
}