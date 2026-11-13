package com.nexusmaven;

import java.util.ArrayList;
import java.util.List;

public class LocacaoService {
    public static final String ARQUIVO_LOCACOES = "locacoes.json";

    // Gera o próximo ID (Ex: LOC-001, LOC-002)
    public String gerarProximoId() {
        List<ProcessoLocacao> processos = JsonUtil.ler(ARQUIVO_LOCACOES, ProcessoLocacao.class);
        int proximo = processos.stream()
                .mapToInt(p -> {
                    try {
                        return Integer.parseInt(p.getId().replace("LOC-", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max().orElse(0) + 1;
        return String.format("LOC-%03d", proximo);
    }

    public void cadastrar(ProcessoLocacao processo) {
        List<ProcessoLocacao> processos = JsonUtil.ler(ARQUIVO_LOCACOES, ProcessoLocacao.class);
        processos.add(processo);
        JsonUtil.salvar(ARQUIVO_LOCACOES, processos);
    }

    public void atualizar(ProcessoLocacao processoAtualizado) {
        List<ProcessoLocacao> processos = JsonUtil.ler(ARQUIVO_LOCACOES, ProcessoLocacao.class);
        for (int i = 0; i < processos.size(); i++) {
            if (processos.get(i).getId().equals(processoAtualizado.getId())) {
                processos.set(i, processoAtualizado);
                break;
            }
        }
        JsonUtil.salvar(ARQUIVO_LOCACOES, processos);
    }
}