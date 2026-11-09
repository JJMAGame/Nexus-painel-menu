package com.nexusmaven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UsuarioEImovelTest {

    // ===== Usuario =====

    @Test
    public void usuarioValidoNormalizaNomeEEmail() {
        Usuario u = new Usuario("  Maria  ", "  Maria@Nexus.COM ", "abc123");
        assertEquals("Maria", u.getNome());
        assertEquals("maria@nexus.com", u.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nomeVazioERecusado() {
        new Usuario("   ", "a@a.com", "123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emailSemArrobaERecusado() {
        new Usuario("Ana", "sem-arroba", "123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emailSemDominioERecusado() {
        new Usuario("Ana", "ana@", "123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emailComEspacoNoMeioERecusado() {
        new Usuario("Ana", "ana @nexus.com", "123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void senhaComCincoCaracteresERecusada() {
        new Usuario("Ana", "ana@nexus.com", "12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void senhaComTrezeCaracteresERecusada() {
        new Usuario("Ana", "ana@nexus.com", "1234567890123");
    }

    @Test
    public void senhasNosLimitesSaoAceitas() {
        new Usuario("Ana", "ana@nexus.com", "123456");        // 6
        new Usuario("Ana", "ana@nexus.com", "123456789012");  // 12
    }

    @Test
    public void autenticarIgnoraMaiusculasNoEmailMasNaoNaSenha() {
        Usuario u = new Usuario("Ana", "ana@nexus.com", "Senha123");
        assertTrue(u.autenticar("ANA@NEXUS.COM", "Senha123"));
        assertFalse(u.autenticar("ana@nexus.com", "senha123"));
        assertFalse(u.autenticar("outro@nexus.com", "Senha123"));
        assertFalse(u.autenticar(null, null));
    }

    @Test
    public void usuarioLidoDeJsonIncompletoNaoQuebraNoAutenticar() {
        assertFalse(new Usuario().autenticar("a@a.com", "123456"));
    }

    // ===== Imovel =====

    @Test
    public void imovelValidoRemoveEspacosDasPontas() {
        Imovel i = new Imovel("  Casa 1 ", " Rua A, 10 ");
        assertEquals("Casa 1", i.getNome());
        assertEquals("Rua A, 10", i.getLocalizacao());
    }

    @Test(expected = IllegalArgumentException.class)
    public void imovelSemNomeERecusado() {
        new Imovel("", "Rua A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void imovelSemLocalizacaoERecusado() {
        new Imovel("Casa 1", null);
    }
}
