package com.nexusmaven;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * Lista de imóveis com situação (Alugado ou Disponível), busca, ordenação e resumo.
 * Um imóvel conta como alugado quando já tem alguma solicitação de pagamento.
 *
 * @author Melqui
 */
public class MenuImoveis {

    private final Scanner scanner;
    private final String arquivoImoveis;
    private final String arquivoCobrancas;

    public MenuImoveis(Scanner scanner) {
        this(scanner, JsonUtil.ARQUIVO_IMOVEIS, MenuCobrancas.ARQUIVO_COBRANCAS);
    }

    MenuImoveis(Scanner scanner, String arquivoImoveis, String arquivoCobrancas) {
        this.scanner = scanner;
        this.arquivoImoveis = arquivoImoveis;
        this.arquivoCobrancas = arquivoCobrancas;
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ IMÓVEIS ------");
            System.out.println("1 - Listar todos");
            System.out.println("2 - Só disponíveis");
            System.out.println("3 - Só alugados");
            System.out.println("4 - Buscar por nome ou localização");
            System.out.println("5 - Ordenar por valor do aluguel");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: listar("TODOS", carregar()); break;
                    case 2: listar("DISPONÍVEIS", porSituacao(false)); break;
                    case 3: listar("ALUGADOS", porSituacao(true)); break;
                    case 4: buscar(); break;
                    case 5: {
                        List<Imovel> lista = carregar();
                        lista.sort(Comparator.comparingDouble(Imovel::getValorAluguel));
                        listar("POR VALOR DO ALUGUEL", lista);
                        break;
                    }
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private List<Imovel> carregar() {
        return JsonUtil.ler(arquivoImoveis, Imovel.class);
    }

    private Set<String> alugados() {
        Set<String> nomes = new HashSet<>();
        for (Cobranca c : JsonUtil.ler(arquivoCobrancas, Cobranca.class)) {
            nomes.add(c.getNomeImovel().toLowerCase(Locale.ROOT));
        }
        return nomes;
    }

    boolean estaAlugado(Imovel imovel, Set<String> alugados) {
        return alugados.contains(imovel.getNome().toLowerCase(Locale.ROOT));
    }

    private List<Imovel> porSituacao(boolean alugado) {
        Set<String> alugados = alugados();
        List<Imovel> resultado = new ArrayList<>();
        for (Imovel i : carregar()) {
            if (estaAlugado(i, alugados) == alugado) resultado.add(i);
        }
        return resultado;
    }

    private void buscar() {
        String termo = EntradaTexto.perguntar(scanner, "Digite parte do nome ou da localização: ");
        if (termo == null) return;
        String procurado = termo.toLowerCase(Locale.ROOT);
        List<Imovel> resultado = new ArrayList<>();
        for (Imovel i : carregar()) {
            if (i.getNome().toLowerCase(Locale.ROOT).contains(procurado)
                    || i.getLocalizacao().toLowerCase(Locale.ROOT).contains(procurado)) {
                resultado.add(i);
            }
        }
        listar("BUSCA: \"" + termo + "\"", resultado);
    }

    void listar(String titulo, List<Imovel> lista) {
        System.out.println("\n--- IMÓVEIS: " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhum imóvel encontrado.");
            return;
        }
        Set<String> alugados = alugados();
        int qtdAlugados = 0;
        double somaAluguel = 0;
        for (int n = 0; n < lista.size(); n++) {
            Imovel i = lista.get(n);
            boolean alugado = estaAlugado(i, alugados);
            if (alugado) qtdAlugados++;
            somaAluguel += i.getValorAluguel();
            System.out.println("[" + (n + 1) + "] " + i.getNome() + "  •  " + (alugado ? "Alugado" : "Disponível"));
            System.out.println("     📍 " + i.getLocalizacao());
            System.out.println("     💰 Aluguel: " + Moeda.formatar(i.getValorAluguel())
                    + "   CPF vinculado: " + i.getCpfProprietario());
        }
        System.out.println("-------------------------------------------");
        System.out.println(lista.size() + " imóvel(is) | " + qtdAlugados + " alugado(s) | "
                + (lista.size() - qtdAlugados) + " disponível(is)");
        System.out.println("Soma dos aluguéis: " + Moeda.formatar(Moeda.arredondar(somaAluguel))
                + " | Média: " + Moeda.formatar(Moeda.arredondar(somaAluguel / lista.size())));
    }
}
