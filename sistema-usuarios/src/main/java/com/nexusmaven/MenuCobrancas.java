package com.nexusmaven;

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Solicitações de pagamento (cobranças de aluguel): gerar, filtrar por situação
 * e registrar o pagamento com o cálculo de multa e juros e a emissão do recibo.
 *
 * @author Melqui
 */
public class MenuCobrancas {

    public static final String ARQUIVO_COBRANCAS = "cobrancas.json";
    private static final int DIA_VENCIMENTO_PADRAO = 10;

    private final Scanner scanner;
    private final String arquivoCobrancas;
    private final String arquivoUsuarios;
    private final String arquivoImoveis;
    private final Supplier<LocalDate> hoje;
    private final RegraDeEncargos regra;

    public MenuCobrancas(Scanner scanner) {
        this(scanner, ARQUIVO_COBRANCAS, JsonUtil.ARQUIVO_USUARIOS, JsonUtil.ARQUIVO_IMOVEIS,
                LocalDate::now, new EncargosContratuais());
    }

    MenuCobrancas(Scanner scanner, String arquivoCobrancas, String arquivoUsuarios, String arquivoImoveis,
                  Supplier<LocalDate> hoje, RegraDeEncargos regra) {
        this.scanner = scanner;
        this.arquivoCobrancas = arquivoCobrancas;
        this.arquivoUsuarios = arquivoUsuarios;
        this.arquivoImoveis = arquivoImoveis;
        this.hoje = hoje;
        this.regra = regra;
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ SOLICITAÇÕES DE PAGAMENTO ------");
            System.out.println("1 - Nova solicitação de pagamento");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Filtrar: Em dia (pagas)");
            System.out.println("4 - Filtrar: A vencer");
            System.out.println("5 - Filtrar: Atrasadas (com multa e juros)");
            System.out.println("6 - Registrar pagamento e emitir recibo");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: novaSolicitacao(); break;
                    case 2: listar(null); break;
                    case 3: listar(StatusCobranca.EM_DIA); break;
                    case 4: listar(StatusCobranca.A_VENCER); break;
                    case 5: listar(StatusCobranca.ATRASADO); break;
                    case 6: registrarPagamento(); break;
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------
    // Nova solicitação
    // ---------------------------------------------------------------
    void novaSolicitacao() {
        List<Usuario> inquilinos = new ArrayList<>();
        for (Usuario u : JsonUtil.ler(arquivoUsuarios, Usuario.class)) {
            if (u instanceof inquilino) inquilinos.add(u);
        }
        List<Imovel> imoveis = JsonUtil.ler(arquivoImoveis, Imovel.class);
        if (inquilinos.isEmpty() || imoveis.isEmpty()) {
            System.out.println("❌ Cadastre ao menos um inquilino e um imóvel antes (opção Gerar novos dados).");
            return;
        }

        int iInq = EntradaTexto.escolher(scanner, "\nQual inquilino?", inquilinos,
                u -> u.getNome() + " (CPF " + u.getCpf() + ")");
        if (iInq < 0) return;
        int iImo = EntradaTexto.escolher(scanner, "\nQual imóvel?", imoveis,
                i -> i.getNome() + " - " + Moeda.formatar(i.getValorAluguel()));
        if (iImo < 0) return;

        String mesTexto = EntradaTexto.perguntar(scanner, "Mês de referência (mm/aaaa): ");
        if (mesTexto == null) return;
        YearMonth mes = Datas.lerMes(mesTexto);

        String diaTexto = EntradaTexto.perguntar(scanner,
                "Dia do vencimento (Enter = " + DIA_VENCIMENTO_PADRAO + "): ");
        if (diaTexto == null) return;
        int dia = diaTexto.isEmpty() ? DIA_VENCIMENTO_PADRAO : lerDia(diaTexto, mes);
        LocalDate vencimento = mes.atDay(dia);

        Usuario inquilino = inquilinos.get(iInq);
        Imovel imovel = imoveis.get(iImo);
        List<Cobranca> cobrancas = JsonUtil.ler(arquivoCobrancas, Cobranca.class);

        boolean repetida = cobrancas.stream().anyMatch(c ->
                c.getCpfInquilino().equals(inquilino.getCpf())
                && c.getNomeImovel().equalsIgnoreCase(imovel.getNome())
                && c.getMesReferencia().equals(Datas.formatar(mes)));
        if (repetida) {
            System.out.println("❌ Já existe uma solicitação de " + Datas.formatar(mes)
                    + " para este inquilino neste imóvel.");
            return;
        }

        Cobranca nova = new Cobranca(proximoId(cobrancas), inquilino.getCpf(), inquilino.getNome(),
                imovel.getNome(), mes, imovel.getValorAluguel(), vencimento);
        cobrancas.add(nova);
        JsonUtil.salvar(arquivoCobrancas, cobrancas);

        System.out.println("\n✅ Solicitação nº " + nova.getId() + " criada.");
        System.out.println("   " + nova.getNomeInquilino() + " | " + nova.getDescricao());
        System.out.println("   Valor: " + Moeda.formatar(nova.getValor())
                + " | Vencimento: " + Datas.formatar(nova.getVencimento()));
    }

    private static int lerDia(String texto, YearMonth mes) {
        try {
            int dia = Integer.parseInt(texto);
            if (dia >= 1 && dia <= mes.lengthOfMonth()) return dia;
        } catch (NumberFormatException e) {
            // cai na mensagem abaixo
        }
        throw new IllegalArgumentException("Dia inválido para " + Datas.formatar(mes) + ".");
    }

    // ---------------------------------------------------------------
    // Listagem com filtro
    // ---------------------------------------------------------------
    void listar(StatusCobranca filtro) {
        LocalDate data = hoje.get();
        List<Cobranca> lista = new ArrayList<>();
        for (Cobranca c : JsonUtil.ler(arquivoCobrancas, Cobranca.class)) {
            if (filtro == null || c.getStatus(data) == filtro) lista.add(c);
        }
        lista.sort(Comparator.comparing(Cobranca::getVencimento));

        String titulo = filtro == null ? "TODAS" : filtro.getRotulo().toUpperCase();
        System.out.println("\n--- SOLICITAÇÕES DE PAGAMENTO: " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            return;
        }

        String formato = "%-4s %-20s %-22s %-8s %-11s %14s  %-10s %s%n";
        System.out.printf(formato, "Nº", "Inquilino", "Imóvel", "Mês", "Vencimento", "Valor", "Situação", "Detalhe");
        double totalValor = 0;
        double totalDevido = 0;
        for (Cobranca c : lista) {
            String detalhe;
            switch (c.getStatus(data)) {
                case EM_DIA:
                    detalhe = "Pago " + Moeda.formatar(c.getValorPago()) + " em " + Datas.formatar(c.getDataPagamento())
                            + " (" + c.getFormaPagamento().getRotulo() + ", recibo " + c.getNumeroRecibo() + ")";
                    break;
                case ATRASADO:
                    double devido = c.getTotalDevido(data, regra);
                    totalDevido += devido;
                    detalhe = c.diasDeAtraso(data) + " dias | multa " + Moeda.formatar(c.getMulta(data, regra))
                            + " + juros " + Moeda.formatar(c.getJuros(data, regra)) + " = " + Moeda.formatar(devido);
                    break;
                default:
                    detalhe = "";
                    totalDevido += c.getValor();
            }
            totalValor += c.getValor();
            System.out.printf(formato, c.getId(), EntradaTexto.cortar(c.getNomeInquilino(), 20),
                    EntradaTexto.cortar(c.getNomeImovel(), 22), c.getMesReferencia(),
                    Datas.formatar(c.getVencimento()), Moeda.formatar(c.getValor()), c.getSituacao(data), detalhe);
        }
        System.out.println("-------------------------------------------");
        System.out.println(lista.size() + " solicitação(ões) | Valor dos aluguéis: " + Moeda.formatar(totalValor));
        if (filtro != StatusCobranca.EM_DIA && totalDevido > 0) {
            System.out.println("Total em aberto hoje (com encargos): " + Moeda.formatar(totalDevido));
        }
    }

    // ---------------------------------------------------------------
    // Registrar pagamento
    // ---------------------------------------------------------------
    void registrarPagamento() {
        LocalDate data = hoje.get();
        List<Cobranca> cobrancas = JsonUtil.ler(arquivoCobrancas, Cobranca.class);
        List<Cobranca> abertas = new ArrayList<>();
        for (Cobranca c : cobrancas) {
            if (!c.estaPago()) abertas.add(c);
        }
        if (abertas.isEmpty()) {
            System.out.println("✅ Não há solicitações em aberto.");
            return;
        }
        abertas.sort(Comparator.comparing(Cobranca::getVencimento));

        int i = EntradaTexto.escolher(scanner, "\nQual solicitação está sendo paga?", abertas,
                c -> "Nº " + c.getId() + " | " + c.getNomeInquilino() + " | " + c.getDescricao()
                        + " | " + c.getSituacao(data) + " | " + Moeda.formatar(c.getTotalDevido(data, regra)));
        if (i < 0) return;
        Cobranca cobranca = abertas.get(i);

        double multa = cobranca.getMulta(data, regra);
        double juros = cobranca.getJuros(data, regra);
        double total = cobranca.getTotalDevido(data, regra);
        System.out.println("\nAluguel:          " + Moeda.formatar(cobranca.getValor()));
        System.out.println("Dias de atraso:   " + cobranca.diasDeAtraso(data));
        System.out.println("Multa (10%):      " + Moeda.formatar(multa));
        System.out.println("Juros (1% a.m.):  " + Moeda.formatar(juros));
        System.out.println("TOTAL A RECEBER:  " + Moeda.formatar(total));

        FormaPagamento[] formas = FormaPagamento.values();
        int f = EntradaTexto.escolher(scanner, "\nForma de pagamento:", List.of(formas), FormaPagamento::getRotulo);
        if (f < 0) return;

        if (!EntradaTexto.confirmar(scanner, "Confirmar o recebimento de " + Moeda.formatar(total) + "?")) {
            System.out.println("Pagamento não registrado.");
            return;
        }

        int recibo = cobrancas.stream().mapToInt(Cobranca::getNumeroRecibo).max().orElse(0) + 1;
        cobranca.registrarPagamento(data, formas[f], total, recibo);
        JsonUtil.salvar(arquivoCobrancas, cobrancas);
        imprimirRecibo(cobranca, multa, juros);
    }

    private static void imprimirRecibo(Cobranca c, double multa, double juros) {
        System.out.println("\n===========================================");
        System.out.println("   FUNDAÇÃO MANUEL CRUZ - RECIBO Nº " + String.format("%06d", c.getNumeroRecibo()));
        System.out.println("===========================================");
        System.out.println("Recebemos de " + c.getNomeInquilino() + " (CPF " + c.getCpfInquilino() + ")");
        System.out.println("a importância de " + Moeda.formatar(c.getValorPago()) + ", referente ao");
        System.out.println(c.getDescricao() + ".");
        System.out.println("  Aluguel: " + Moeda.formatar(c.getValor())
                + " | Multa: " + Moeda.formatar(multa) + " | Juros: " + Moeda.formatar(juros));
        System.out.println("Forma de pagamento: " + c.getFormaPagamento().getRotulo()
                + " | Data: " + Datas.formatar(c.getDataPagamento()));
        System.out.println("===========================================");
        System.out.println("✅ Pagamento registrado.");
    }

    private static int proximoId(List<? extends Lancamento> lista) {
        return lista.stream().mapToInt(Lancamento::getId).max().orElse(0) + 1;
    }
}
