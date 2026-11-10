package com.nexusmaven;

/**
 * Situação de uma despesa da Fundação.
 *
 * @author Melqui
 */
public enum StatusDespesa {
    AGUARDANDO_APROVACAO("Aguardando aprovação"),
    A_PAGAR("A pagar"),
    PAGO("Pago");

    private final String rotulo;

    StatusDespesa(String rotulo) { this.rotulo = rotulo; }

    public String getRotulo() { return rotulo; }
}
