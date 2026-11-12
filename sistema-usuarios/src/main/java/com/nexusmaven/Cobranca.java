package com.nexusmaven;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Solicitação de pagamento do aluguel de um inquilino em um mês.
 * O valor vem do cadastro do imóvel; multa e juros são calculados na hora do pagamento.
 *
 * @author Melqui
 */
public class Cobranca extends Lancamento {

    private String cpfInquilino;
    private String nomeInquilino;
    private String nomeImovel;
    private String mesReferencia;
    private FormaPagamento formaPagamento;
    private double valorPago;
    private int numeroRecibo;

    public Cobranca() {}

    public Cobranca(int id, String cpfInquilino, String nomeInquilino, String nomeImovel,
                    YearMonth mesReferencia, double valorAluguel, LocalDate vencimento) {
        super(id, valorAluguel, vencimento);
        this.cpfInquilino = obrigatorio(cpfInquilino, "o CPF do inquilino");
        this.nomeInquilino = obrigatorio(nomeInquilino, "o nome do inquilino");
        this.nomeImovel = obrigatorio(nomeImovel, "o imóvel");
        if (mesReferencia == null) throw new IllegalArgumentException("Informe o mês de referência.");
        this.mesReferencia = Datas.formatar(mesReferencia);
    }

    public String getCpfInquilino() { return cpfInquilino; }
    public String getNomeInquilino() { return nomeInquilino; }
    public String getNomeImovel() { return nomeImovel; }
    public String getMesReferencia() { return mesReferencia; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public double getValorPago() { return valorPago; }
    public int getNumeroRecibo() { return numeroRecibo; }

    /** Em dia = já paga; A vencer = em aberto e no prazo; Atrasado = em aberto e vencida. */
    public StatusCobranca getStatus(LocalDate hoje) {
        if (estaPago()) return StatusCobranca.EM_DIA;
        return estaVencido(hoje) ? StatusCobranca.ATRASADO : StatusCobranca.A_VENCER;
    }

    public double getMulta(LocalDate hoje, RegraDeEncargos regra) {
        return regra.multa(getValor(), diasDeAtraso(hoje));
    }

    public double getJuros(LocalDate hoje, RegraDeEncargos regra) {
        return regra.juros(getValor(), diasDeAtraso(hoje));
    }

    /** Quanto o inquilino precisa pagar hoje: aluguel + multa + juros. */
    public double getTotalDevido(LocalDate hoje, RegraDeEncargos regra) {
        return estaPago() ? 0 : regra.totalComEncargos(getValor(), diasDeAtraso(hoje));
    }

    public void registrarPagamento(LocalDate data, FormaPagamento forma, double valorPago, int numeroRecibo) {
        if (forma == null) throw new IllegalArgumentException("Informe a forma de pagamento.");
        if (valorPago <= 0) throw new IllegalArgumentException("O valor pago deve ser maior que zero.");
        if (numeroRecibo <= 0) throw new IllegalArgumentException("Número de recibo inválido.");
        marcarComoPago(data);
        this.formaPagamento = forma;
        this.valorPago = Moeda.arredondar(valorPago);
        this.numeroRecibo = numeroRecibo;
    }

    @Override
    public String getDescricao() {
        return "Aluguel " + mesReferencia + " - " + nomeImovel;
    }

    @Override
    public String getSituacao(LocalDate hoje) {
        return getStatus(hoje).getRotulo();
    }

    private static String obrigatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) throw new IllegalArgumentException("Informe " + campo + ".");
        return texto.trim();
    }
}
