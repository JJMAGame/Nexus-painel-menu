package com.nexusmaven;

/**
 * Formas de pagamento aceitas na Secretaria.
 *
 * @author Melqui
 */
public enum FormaPagamento {
    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    TRANSFERENCIA("Transferência"),
    DEPOSITO("Depósito");

    private final String rotulo;

    FormaPagamento(String rotulo) { this.rotulo = rotulo; }

    public String getRotulo() { return rotulo; }
}
