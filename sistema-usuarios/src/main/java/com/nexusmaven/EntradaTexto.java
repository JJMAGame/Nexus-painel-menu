package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

/**
 * Perguntas comuns aos menus de administração: texto, número e escolha em lista.
 * Todas devolvem null quando a entrada termina, para o menu poder encerrar sem erro.
 *
 * @author Melqui
 */
final class EntradaTexto {

    private EntradaTexto() {}

    static String perguntar(Scanner scanner, String pergunta) {
        System.out.print(pergunta);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    /** Mostra a lista numerada a partir de 1 e devolve o índice escolhido (base 0) ou -1. */
    static <T> int escolher(Scanner scanner, String titulo, List<T> itens, java.util.function.Function<T, String> descricao) {
        System.out.println(titulo);
        for (int i = 0; i < itens.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + descricao.apply(itens.get(i)));
        }
        String resposta = perguntar(scanner, "Escolha o número: ");
        if (resposta == null) return -1;
        try {
            int numero = Integer.parseInt(resposta);
            if (numero >= 1 && numero <= itens.size()) return numero - 1;
        } catch (NumberFormatException e) {
            // cai na mensagem abaixo
        }
        System.out.println("❌ Escolha um número da lista.");
        return -1;
    }

    /** Corta textos longos para caberem nas colunas das listas. */
    static String cortar(String texto, int tamanho) {
        if (texto == null) return "";
        return texto.length() <= tamanho ? texto : texto.substring(0, tamanho - 1) + "…";
    }

    static boolean confirmar(Scanner scanner, String pergunta) {
        String resposta = perguntar(scanner, pergunta + " (S/N): ");
        return resposta != null && resposta.equalsIgnoreCase("S");
    }
}
