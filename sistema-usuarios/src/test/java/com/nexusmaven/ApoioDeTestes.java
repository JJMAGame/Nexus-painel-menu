package com.nexusmaven;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** Funções comuns dos testes: simular o teclado e capturar o que vai para a tela. */
final class ApoioDeTestes {

    private ApoioDeTestes() {}

    /** Cada argumento é uma linha digitada; depois da última, a entrada termina. */
    static Scanner entrada(String... linhas) {
        String texto = String.join("\n", linhas) + "\n";
        return new Scanner(new ByteArrayInputStream(texto.getBytes(UTF_8)), UTF_8);
    }

    static String capturarSaida(Runnable acao) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, UTF_8));
        try {
            acao.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(UTF_8);
    }
}
