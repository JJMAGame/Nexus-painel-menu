package com.nexusmaven;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class EntradaConsole {
    public static final int OPCAO_INVALIDA = -1;

    private EntradaConsole() {}

    public static Scanner criarScanner() {
        // Força o Scanner a ler o teclado como UTF-8
        return new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public static int lerOpcao(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return 0;
        }
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return OPCAO_INVALIDA;
        }
    }
}
