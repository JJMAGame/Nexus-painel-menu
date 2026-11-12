package com.nexusmaven;

/**
 * Tipos de despesa da Fundação.
 *
 * @author Melqui
 */
public enum CategoriaDespesa {
    AGUA("Água"),
    ENERGIA("Energia"),
    INTERNET("Internet"),
    TELEFONE("Telefone"),
    COMBUSTIVEL("Combustível"),
    MANUTENCAO("Manutenção"),
    LIMPEZA("Limpeza"),
    FOLHA("Folha e encargos"),
    CONTABILIDADE("Contabilidade"),
    VIGILANCIA("Vigilância"),
    OUTRAS("Outras");

    private final String rotulo;

    CategoriaDespesa(String rotulo) { this.rotulo = rotulo; }

    public String getRotulo() { return rotulo; }
}
