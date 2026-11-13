package com.nexusmaven;

/**
 * Situação de uma solicitação de pagamento.
 *
 * @author Melqui
 */
public enum StatusCobranca {
    EM_DIA("Em dia"),
    A_VENCER("A vencer"),
    ATRASADO("Atrasado");

    private final String rotulo;

    StatusCobranca(String rotulo) { this.rotulo = rotulo; }

    public String getRotulo() { return rotulo; }
}
