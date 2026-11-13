package com.nexusmaven;

public enum EtapaLocacao {
    PROPOSTA("Proposta"),
    DOCUMENTOS("Documentos"),
    CONSULTA_CPF("Consulta CPF"),
    ANALISE_RENDA("Renda"),
    APROVACAO("Aprovação"),
    VISTORIA("Vistoria"),
    CONTRATO("Contrato"),
    CHAVES("Chaves"),
    CONCLUIDA("Concluída");

    private final String rotulo;

    EtapaLocacao(String rotulo) { this.rotulo = rotulo; }
    public String getRotulo() { return rotulo; }

    public static EtapaLocacao porRotulo(String rotulo) {
        for (EtapaLocacao e : values()) {
            if (e.getRotulo().equalsIgnoreCase(rotulo) || e.name().equalsIgnoreCase(rotulo)) return e;
        }
        throw new IllegalArgumentException("Etapa inválida: " + rotulo);
    }
}