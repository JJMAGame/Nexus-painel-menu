package com.nexusmaven;

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Despesas da Fundação: lançar, aprovar, registrar pagamento e filtrar por situação
 * (Pago, A pagar e Aguardando aprovação).
 *
 * @author Melqui
 */
public class MenuDespesas {

    public static final String ARQUIVO_DESPESAS = "despesas.json";

    private final Scanner scanner;
    private final String arquivoDespesas;
    private final String nomeUsuario;
    private final Supplier<LocalDate> hoje;

    public MenuDespesas(Scanner scanner, String nomeUsuario) {
        this(scanner, ARQUIVO_DESPESAS, nomeUsuario, LocalDate::now);
    }

    MenuDespesas(Scanner scanner, String arquivoDespesas, String nomeUsuario, Supplier<LocalDate> hoje) {
        this.scanner = scanner;
        this.arquivoDespesas = arquivoDespesas;
        this.nomeUsuario = nomeUsuario;
        this.hoje = hoje;
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ DESPESAS DA FUNDAÇÃO ------");
            System.out.println("1 - Lançar despesa");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Filtrar: Pago");
            System.out.println("4 - Filtrar: A pagar");
            System.out.println("5 - Filtrar: Aguardando aprovação");
            System.out.println("6 - Aprovar despesa");
            System.out.println("7 - Registrar pagamento de despesa");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: lancar(); break;
                    case 2: listar(null); break;
                    case 3: listar(StatusDespesa.PAGO); break;
                    case 4: listar(StatusDespesa.A_PAGAR); break;
                    case 5: listar(StatusDespesa.AGUARDANDO_APROVACAO); break;
                    case 6: aprovar(); break;
                    case 7: registrarPagamento(); break;
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    void lancar() {
        String descricao = EntradaTexto.perguntar(scanner, "Descrição (ex.: Energia da sede, out/2026): ");
        if (descricao == null) return;

        CategoriaDespesa[] categorias = CategoriaDespesa.values();
        int c = EntradaTexto.escolher(scanner, "\nCategoria:", List.of(categorias), CategoriaDespesa::getRotulo);
        if (c < 0) return;

        String valorTexto = EntradaTexto.perguntar(scanner, "Valor: R$ ");
        if (valorTexto == null) return;
        double valor = Moeda.ler(valorTexto);

        String vencTexto = EntradaTexto.perguntar(scanner, "Vencimento (dd/mm/aaaa): ");
        if (vencTexto == null) return;
        LocalDate vencimento = Datas.lerData(vencTexto);

        List<Despesa> despesas = JsonUtil.ler(arquivoDespesas, Despesa.class);
        int id = despesas.stream().mapToInt(Lancamento::getId).max().orElse(0) + 1;
        Despesa nova = new Despesa(id, descricao, categorias[c], valor, vencimento, nomeUsuario);
        despesas.add(nova);
        JsonUtil.salvar(arquivoDespesas, despesas);

        System.out.println("\n✅ Despesa nº " + id + " lançada: " + nova.getDescricao() + " - " + Moeda.formatar(valor));
        System.out.println("   Situação: " + nova.getStatus().getRotulo()
                + ". O pagamento só é liberado depois da aprovação.");
    }

    void listar(StatusDespesa filtro) {
        LocalDate data = hoje.get();
        List<Despesa> lista = new ArrayList<>();
        for (Despesa d : JsonUtil.ler(arquivoDespesas, Despesa.class)) {
            if (filtro == null || d.getStatus() == filtro) lista.add(d);
        }
        lista.sort(Comparator.comparing(Despesa::getVencimento));

        String titulo = filtro == null ? "TODAS" : filtro.getRotulo().toUpperCase();
        System.out.println("\n--- DESPESAS: " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        String formato = "%-4s %-30s %-17s %-11s %14s  %s%n";
        System.out.printf(formato, "Nº", "Descrição", "Categoria", "Vencimento", "Valor", "Situação");
        double total = 0;
        for (Despesa d : lista) {
            total += d.getValor();
            System.out.printf(formato, d.getId(), EntradaTexto.cortar(d.getDescricao(), 30),
                    d.getCategoria().getRotulo(), Datas.formatar(d.getVencimento()),
                    Moeda.formatar(d.getValor()), d.getSituacao(data));
        }
        System.out.println("-------------------------------------------");
        System.out.println(lista.size() + " despesa(s) | Total: " + Moeda.formatar(Moeda.arredondar(total)));
    }

    void aprovar() {
        List<Despesa> despesas = JsonUtil.ler(arquivoDespesas, Despesa.class);
        List<Despesa> pendentes = filtrar(despesas, StatusDespesa.AGUARDANDO_APROVACAO);
        if (pendentes.isEmpty()) {
            System.out.println("✅ Nenhuma despesa aguardando aprovação.");
            return;
        }
        int i = EntradaTexto.escolher(scanner, "\nQual despesa aprovar?", pendentes, this::resumo);
        if (i < 0) return;
        Despesa despesa = pendentes.get(i);
        if (!EntradaTexto.confirmar(scanner, "Aprovar " + despesa.getDescricao() + "?")) {
            System.out.println("Nada foi alterado.");
            return;
        }
        despesa.aprovar(nomeUsuario);
        JsonUtil.salvar(arquivoDespesas, despesas);
        System.out.println("✅ Despesa aprovada. Situação: " + despesa.getStatus().getRotulo() + ".");
    }

    void registrarPagamento() {
        List<Despesa> despesas = JsonUtil.ler(arquivoDespesas, Despesa.class);
        List<Despesa> aPagar = filtrar(despesas, StatusDespesa.A_PAGAR);
        if (aPagar.isEmpty()) {
            System.out.println("✅ Nenhuma despesa a pagar. Despesas novas precisam ser aprovadas antes.");
            return;
        }
        int i = EntradaTexto.escolher(scanner, "\nQual despesa foi paga?", aPagar, this::resumo);
        if (i < 0) return;
        Despesa despesa = aPagar.get(i);
        if (!EntradaTexto.confirmar(scanner, "Registrar o pagamento de " + Moeda.formatar(despesa.getValor()) + "?")) {
            System.out.println("Nada foi alterado.");
            return;
        }
        despesa.registrarPagamento(hoje.get());
        JsonUtil.salvar(arquivoDespesas, despesas);
        System.out.println("✅ Pagamento registrado em " + Datas.formatar(despesa.getDataPagamento()) + ".");
    }

    private String resumo(Despesa d) {
        return "Nº " + d.getId() + " | " + d.getDescricao() + " | " + d.getCategoria().getRotulo()
                + " | " + Moeda.formatar(d.getValor()) + " | vence " + Datas.formatar(d.getVencimento());
    }

    private static List<Despesa> filtrar(List<Despesa> despesas, StatusDespesa status) {
        List<Despesa> resultado = new ArrayList<>();
        for (Despesa d : despesas) {
            if (d.getStatus() == status) resultado.add(d);
        }
        return resultado;
    }
}
