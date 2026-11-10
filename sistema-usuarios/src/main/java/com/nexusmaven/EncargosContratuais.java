package com.nexusmaven;

/**
 * Regra usada nos contratos da Fundação: multa de 10% e juros de 1% ao mês,
 * proporcionais aos dias de atraso.
 *
 * @author Melqui
 */
public class EncargosContratuais implements RegraDeEncargos {

    public static final double PERCENTUAL_MULTA = 0.10;
    public static final double JUROS_AO_MES = 0.01;

    @Override
    public double multa(double valor, long diasDeAtraso) {
        return diasDeAtraso > 0 ? Moeda.arredondar(valor * PERCENTUAL_MULTA) : 0;
    }

    @Override
    public double juros(double valor, long diasDeAtraso) {
        return diasDeAtraso > 0 ? Moeda.arredondar(valor * JUROS_AO_MES * diasDeAtraso / 30.0) : 0;
    }
}
