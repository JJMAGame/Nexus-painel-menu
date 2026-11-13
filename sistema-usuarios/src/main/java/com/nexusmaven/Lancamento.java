package com.nexusmaven;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Base de tudo o que tem valor, vencimento e pagamento: as solicitações de
 * pagamento dos inquilinos (receitas) e as despesas da Fundação.
 * As datas ficam guardadas como texto (aaaa-mm-dd) para o Gson gravar no JSON.
 *
 * @author Melqui
 */
public abstract class Lancamento {

    private int id;
    private double valor;
    private String vencimento;
    private String dataPagamento;

    protected Lancamento() {}

    protected Lancamento(int id, double valor, LocalDate vencimento) {
        setId(id);
        setValor(valor);
        setVencimento(vencimento);
    }

    public int getId() { return id; }
    public double getValor() { return valor; }

    public LocalDate getVencimento() {
        return vencimento == null ? null : LocalDate.parse(vencimento);
    }

    public LocalDate getDataPagamento() {
        return dataPagamento == null ? null : LocalDate.parse(dataPagamento);
    }

    public boolean estaPago() {
        return dataPagamento != null;
    }

    /** Ainda não foi pago e o vencimento já passou. */
    public boolean estaVencido(LocalDate hoje) {
        return !estaPago() && getVencimento() != null && getVencimento().isBefore(hoje);
    }

    public long diasDeAtraso(LocalDate hoje) {
        return estaVencido(hoje) ? ChronoUnit.DAYS.between(getVencimento(), hoje) : 0;
    }

    /** Texto curto que identifica o lançamento nas listas. */
    public abstract String getDescricao();

    /** Situação para exibir ao usuário. Cada tipo de lançamento tem as suas. */
    public abstract String getSituacao(LocalDate hoje);

    protected void marcarComoPago(LocalDate data) {
        if (data == null) throw new IllegalArgumentException("Informe a data do pagamento.");
        if (estaPago()) throw new IllegalStateException("Este lançamento já foi pago.");
        this.dataPagamento = data.toString();
    }

    private void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("O número do lançamento deve ser positivo.");
        this.id = id;
    }

    private void setValor(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        this.valor = Moeda.arredondar(valor);
    }

    private void setVencimento(LocalDate vencimento) {
        if (vencimento == null) throw new IllegalArgumentException("Informe o vencimento.");
        this.vencimento = vencimento.toString();
    }
}
