package com.nexusmaven;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Test;

/** Regras de situação da cobrança (janela de 7 dias) e cálculo de multa/juros. */
public class CobrancaTest {

    private static final RegraDeEncargos REGRA = new EncargosContratuais();
    private static final LocalDate HOJE = LocalDate.of(2026, 11, 18);

    private static Cobranca cobrancaComVencimento(LocalDate vencimento) {
        return new Cobranca(1, "12312312399", "João", "Casa do Pedro",
                YearMonth.from(vencimento), 1000.0, vencimento);
    }

    // ---------- janela de 7 dias ----------

    @Test
    public void semPagarELongeDoVencimentoFicaEmDia() {
        assertEquals(StatusCobranca.EM_DIA, cobrancaComVencimento(HOJE.plusDays(8)).getStatus(HOJE));
    }

    @Test
    public void faltandoSeteDiasFicaAVencer() {
        assertEquals(StatusCobranca.A_VENCER, cobrancaComVencimento(HOJE.plusDays(7)).getStatus(HOJE));
    }

    @Test
    public void vencendoHojeFicaAVencer() {
        assertEquals(StatusCobranca.A_VENCER, cobrancaComVencimento(HOJE).getStatus(HOJE));
    }

    @Test
    public void vencidaHaUmDiaFicaAtrasada() {
        assertEquals(StatusCobranca.ATRASADO, cobrancaComVencimento(HOJE.minusDays(1)).getStatus(HOJE));
    }

    @Test
    public void pagaFicaEmDiaMesmoVencida() {
        Cobranca c = cobrancaComVencimento(HOJE.minusDays(30));
        c.registrarPagamento(HOJE, FormaPagamento.PIX, 1000.0, 1);
        assertEquals(StatusCobranca.EM_DIA, c.getStatus(HOJE));
    }

    // ---------- multa e juros ----------

    @Test
    public void emDiaNaoTemMultaNemJuros() {
        Cobranca c = cobrancaComVencimento(HOJE.plusDays(7));
        assertEquals(0.0, c.getMulta(HOJE, REGRA), 0.001);
        assertEquals(0.0, c.getJuros(HOJE, REGRA), 0.001);
        assertEquals(1000.0, c.getTotalDevido(HOJE, REGRA), 0.001);
    }

    @Test
    public void atrasoDeTrintaDiasCobraMultaEJuros() {
        Cobranca c = cobrancaComVencimento(HOJE.minusDays(30));
        // multa 10% = 100; juros 1% ao mês por 30 dias = 10; total = 1110
        assertEquals(100.0, c.getMulta(HOJE, REGRA), 0.001);
        assertEquals(10.0, c.getJuros(HOJE, REGRA), 0.001);
        assertEquals(1110.0, c.getTotalDevido(HOJE, REGRA), 0.001);
    }
}
