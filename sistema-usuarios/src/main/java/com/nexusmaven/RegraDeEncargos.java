package com.nexusmaven;

/**
 * Como calcular multa e juros de um valor pago com atraso.
 * Separar a regra permite trocar os percentuais sem mexer nas cobranças.
 *
 * @author Melqui
 */
public interface RegraDeEncargos {

    double multa(double valor, long diasDeAtraso);

    double juros(double valor, long diasDeAtraso);

    default double totalComEncargos(double valor, long diasDeAtraso) {
        return Moeda.arredondar(valor + multa(valor, diasDeAtraso) + juros(valor, diasDeAtraso));
    }
}
