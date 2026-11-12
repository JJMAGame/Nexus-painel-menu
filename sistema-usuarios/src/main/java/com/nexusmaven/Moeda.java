package com.nexusmaven;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formata e lê valores em reais no padrão brasileiro.
 * Aceita tanto "1500,50" quanto "1500.50" e "1.500,50" na digitação.
 *
 * @author Melqui
 */
public final class Moeda {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    private Moeda() {}

    /** Ex.: 1500.5 vira "R$ 1.500,50". */
    public static String formatar(double valor) {
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor).replace('\u00A0', ' ');
    }

    /** Converte o texto digitado em valor. Lança IllegalArgumentException com mensagem amigável. */
    public static double ler(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe um valor.");
        }
        String limpo = texto.trim().replace("R$", "").replace(" ", "");
        if (limpo.contains(",")) {
            limpo = limpo.replace(".", "").replace(",", ".");   // 1.500,50 -> 1500.50
        } else if (limpo.matches("\\d{1,3}(\\.\\d{3})+")) {
            limpo = limpo.replace(".", "");                     // 1.500 -> 1500
        }
        double valor;
        try {
            valor = Double.parseDouble(limpo);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido: \"" + texto.trim() + "\". Use, por exemplo, 1500,50.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }
        return arredondar(valor);
    }

    /** Arredonda para centavos. */
    public static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
