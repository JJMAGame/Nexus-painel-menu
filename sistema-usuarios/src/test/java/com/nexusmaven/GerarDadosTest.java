package com.nexusmaven;

import static com.nexusmaven.ApoioDeTestes.capturarSaida;
import static com.nexusmaven.ApoioDeTestes.entrada;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Fluxo do GerarDados do começo ao fim, gravando em bancos temporários. */
public class GerarDadosTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    private String usuarios() { return new File(pasta.getRoot(), "usuarios.json").getPath(); }
    private String imoveis() { return new File(pasta.getRoot(), "imoveis.json").getPath(); }

    private String rodar(String... digitado) {
        return capturarSaida(() -> GerarDados.executar(entrada(digitado), usuarios(), imoveis()));
    }

    @Test
    public void criaInquilinoComoContaPadrao() {
        String saida = rodar("1", "Maria", "maria@nexus.com", "abc12345", "1");

        List<Usuario> lidos = JsonUtil.ler(usuarios(), Usuario.class);
        assertEquals(1, lidos.size());
        assertTrue(lidos.get(0) instanceof Inquilino);
        assertTrue(saida, saida.contains("Tipo: Inquilino"));
    }

    @Test
    public void qualquerRespostaDiferenteDe2ViraInquilino() {
        rodar("1", "Maria", "maria@nexus.com", "abc12345", "");
        assertTrue(JsonUtil.ler(usuarios(), Usuario.class).get(0) instanceof Inquilino);
    }

    @Test
    public void criaAdministrador() {
        String saida = rodar("1", "Chefe", "chefe@nexus.com", "abc12345", "2");

        assertTrue(JsonUtil.ler(usuarios(), Usuario.class).get(0) instanceof Admin);
        assertTrue(saida, saida.contains("Tipo: Administrador"));
    }

    @Test
    public void novoUsuarioNaoApagaOsQueJaExistem() {
        rodar("1", "Um", "um@nexus.com", "abc12345", "1");
        rodar("1", "Dois", "dois@nexus.com", "abc12345", "2");

        List<Usuario> lidos = JsonUtil.ler(usuarios(), Usuario.class);
        assertEquals(2, lidos.size());
        assertEquals("um@nexus.com", lidos.get(0).getEmail());
        assertEquals("dois@nexus.com", lidos.get(1).getEmail());
    }

    @Test
    public void emailRepetidoERecusadoMesmoComMaiusculas() {
        rodar("1", "Um", "um@nexus.com", "abc12345", "1");
        String saida = rodar("1", "Outro", "UM@Nexus.com", "abc12345", "1");

        assertEquals(1, JsonUtil.ler(usuarios(), Usuario.class).size());
        assertTrue(saida, saida.contains("já existe"));
    }

    @Test
    public void senhaCurtaNaoCriaOBanco() {
        String saida = rodar("1", "Maria", "maria@nexus.com", "123", "1");

        assertFalse(new File(usuarios()).exists());
        assertTrue(saida, saida.contains("Senha deve ter entre 6 e 12 caracteres"));
    }

    @Test
    public void criaImovel() {
        String saida = rodar("2", "Casa de José", "R. Ubiratã, 226 - Recife - PE");

        List<Imovel> lidos = JsonUtil.ler(imoveis(), Imovel.class);
        assertEquals(1, lidos.size());
        assertEquals("Casa de José", lidos.get(0).getNome());
        assertTrue(saida, saida.contains("Total de imóveis no sistema: 1"));
    }

    @Test
    public void imovelSemLocalizacaoNaoEGravado() {
        String saida = rodar("2", "Casa", "");

        assertFalse(new File(imoveis()).exists());
        assertTrue(saida, saida.contains("localização"));
    }

    @Test
    public void opcaoInvalidaNaoGeraNada() {
        String saida = rodar("9");

        assertFalse(new File(usuarios()).exists());
        assertFalse(new File(imoveis()).exists());
        assertTrue(saida, saida.contains("Opção inválida"));
    }
}
