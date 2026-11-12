package com.nexusmaven;

import java.time.LocalDate;

/**
 * Conta paga pela Fundação (água, energia, combustível...).
 * Toda despesa nasce aguardando aprovação; depois de aprovada fica a pagar e, por fim, paga.
 *
 * @author Melqui
 */
public class Despesa extends Lancamento {

    private String descricao;
    private CategoriaDespesa categoria;
    private StatusDespesa status;
    private String lancadaPor;
    private String aprovadaPor;

    public Despesa() {}

    public Despesa(int id, String descricao, CategoriaDespesa categoria, double valor,
                   LocalDate vencimento, String lancadaPor) {
        super(id, valor, vencimento);
        if (descricao == null || descricao.trim().isEmpty()) throw new IllegalArgumentException("Informe a descrição da despesa.");
        if (categoria == null) throw new IllegalArgumentException("Informe a categoria da despesa.");
        this.descricao = descricao.trim();
        this.categoria = categoria;
        this.lancadaPor = lancadaPor;
        this.status = StatusDespesa.AGUARDANDO_APROVACAO;
    }

    @Override
    public String getDescricao() { return descricao; }
    public CategoriaDespesa getCategoria() { return categoria; }
    public StatusDespesa getStatus() { return status; }
    public String getLancadaPor() { return lancadaPor; }
    public String getAprovadaPor() { return aprovadaPor; }

    public void aprovar(String quemAprovou) {
        if (status != StatusDespesa.AGUARDANDO_APROVACAO) {
            throw new IllegalStateException("Só despesas aguardando aprovação podem ser aprovadas.");
        }
        this.status = StatusDespesa.A_PAGAR;
        this.aprovadaPor = quemAprovou;
    }

    public void registrarPagamento(LocalDate data) {
        if (status == StatusDespesa.AGUARDANDO_APROVACAO) {
            throw new IllegalStateException("Aprove a despesa antes de registrar o pagamento.");
        }
        marcarComoPago(data);
        this.status = StatusDespesa.PAGO;
    }

    @Override
    public String getSituacao(LocalDate hoje) {
        if (status == StatusDespesa.A_PAGAR && estaVencido(hoje)) return "A pagar (vencida)";
        return status.getRotulo();
    }
}
