package com.nexusmaven;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Leitura e exibição de datas no formato brasileiro (dd/mm/aaaa e mm/aaaa).
 *
 * @author Melqui
 */
public final class Datas {

    private static final DateTimeFormatter DIA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private Datas() {}

    public static LocalDate lerData(String texto) {
        try {
            return LocalDate.parse(texto == null ? "" : texto.trim(), DIA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Use o formato dd/mm/aaaa, por exemplo 10/12/2026.");
        }
    }

    public static YearMonth lerMes(String texto) {
        try {
            return YearMonth.parse(texto == null ? "" : texto.trim(), MES);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Mês inválido. Use o formato mm/aaaa, por exemplo 12/2026.");
        }
    }

    public static String formatar(LocalDate data) {
        return data == null ? "-" : data.format(DIA);
    }

    public static String formatar(YearMonth mes) {
        return mes == null ? "-" : mes.format(MES);
    }
}
