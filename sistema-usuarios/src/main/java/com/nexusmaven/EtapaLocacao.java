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

    /** Próxima etapa na sequência, ou null se já estiver na última (CONCLUIDA). */
    public EtapaLocacao proxima() {
        EtapaLocacao[] etapas = values();
        return ordinal() < etapas.length - 1 ? etapas[ordinal() + 1] : null;
    }

    /** Etapa anterior, ou null se já estiver na primeira (PROPOSTA). */
    public EtapaLocacao anterior() {
        return ordinal() > 0 ? values()[ordinal() - 1] : null;
    }

    /** Pendência criada automaticamente ao entrar nesta etapa; null quando não há. */
    public String pendenciaPadrao() {
        switch (this) {
            case PROPOSTA: return "Aguardando análise da proposta";
            case DOCUMENTOS: return "Entregar RG, CPF e comprovante de renda";
            case CONSULTA_CPF: return "Aguardando consulta de CPF (Serasa/SPC)";
            case ANALISE_RENDA: return "Confirmar renda mínima de 3x o aluguel";
            case APROVACAO: return "Aguardando aprovação da Fundação";
            case VISTORIA: return "Agendar vistoria do imóvel";
            case CONTRATO: return "Assinar contrato e termo de vistoria";
            case CHAVES: return "Vistoria final e entrega das chaves";
            default: return null; // CONCLUIDA não gera pendência
        }
    }

    public static EtapaLocacao porRotulo(String rotulo) {
        for (EtapaLocacao e : values()) {
            if (e.getRotulo().equalsIgnoreCase(rotulo) || e.name().equalsIgnoreCase(rotulo)) return e;
        }
        throw new IllegalArgumentException("Etapa inválida: " + rotulo);
    }
}
