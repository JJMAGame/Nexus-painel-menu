package com.nexusmaven;

import java.util.List;

public class LocacaoService {
    public static final String ARQUIVO_LOCACOES = "locacoes.json";

    private final String arquivoLocacoes;

    public LocacaoService() {
        this(ARQUIVO_LOCACOES);
    }

    // Construtor usado nos testes, para gravar num arquivo temporário.
    LocacaoService(String arquivoLocacoes) {
        this.arquivoLocacoes = arquivoLocacoes;
    }

    public List<ProcessoLocacao> listar() {
        return JsonUtil.ler(arquivoLocacoes, ProcessoLocacao.class);
    }

    // Gera o próximo ID (Ex: LOC-001, LOC-002)
    public String gerarProximoId() {
        List<ProcessoLocacao> processos = listar();
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
        List<ProcessoLocacao> processos = listar();
        processos.add(processo);
        JsonUtil.salvar(arquivoLocacoes, processos);
    }

    public void atualizar(ProcessoLocacao processoAtualizado) {
        List<ProcessoLocacao> processos = listar();
        for (int i = 0; i < processos.size(); i++) {
            if (processos.get(i).getId().equals(processoAtualizado.getId())) {
                processos.set(i, processoAtualizado);
                break;
            }
        }
        JsonUtil.salvar(arquivoLocacoes, processos);
    }

    public void remover(String id) {
        List<ProcessoLocacao> processos = listar();
        processos.removeIf(p -> p.getId().equals(id));
        JsonUtil.salvar(arquivoLocacoes, processos);
    }
}
