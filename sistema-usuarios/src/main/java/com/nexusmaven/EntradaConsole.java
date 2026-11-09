package com.nexusmaven;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Entrada pelo terminal compartilhada por todos os menus.
 * Existe um único Scanner no programa inteiro (criado no Main e repassado),
 * porque vários Scanner sobre o mesmo System.in perdem texto já lido no buffer.
 */
public final class EntradaConsole {

    /** Retornado por {@link #lerOpcao(Scanner)} quando o texto digitado não é um número. */
    public static final int OPCAO_INVALIDA = -1;

    private EntradaConsole() {}

    /** Cria o Scanner lendo o terminal no charset do próprio terminal (no Windows, a página de código do console). */
    public static Scanner criarScanner() {
        return new Scanner(System.in, charsetDoTerminal());
    }

    static Charset charsetDoTerminal() {
        for (String propriedade : new String[] {"stdin.encoding", "native.encoding"}) {
            String nome = System.getProperty(propriedade);
            if (nome != null) {
                try {
                    return Charset.forName(nome);
                } catch (IllegalArgumentException e) {
                    // charset desconhecido neste JDK: tenta a próxima opção
                }
            }
        }
        return Charset.defaultCharset();
    }

    /** Lê uma opção numérica de menu. Fim da entrada vale como 0 (sair). */
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
