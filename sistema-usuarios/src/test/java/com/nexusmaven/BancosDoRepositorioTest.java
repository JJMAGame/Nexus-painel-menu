package com.nexusmaven;

import java.io.IOException;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Assume;
import org.junit.Test;

/**
 * Confere os bancos de verdade do repositório (usuarios.json e imoveis.json, na raiz).
 * O Maven roda os testes dentro de sistema-usuarios/, por isso o "../".
 * Se os arquivos não forem encontrados (outra pasta de trabalho), os testes são ignorados.
 */
public class BancosDoRepositorioTest {

    private static final Path USUARIOS = Path.of("..", JsonUtil.ARQUIVO_USUARIOS);
    private static final Path IMOVEIS = Path.of("..", JsonUtil.ARQUIVO_IMOVEIS);

    private static void estrito(Path arquivo) throws IOException {
        // lança CharacterCodingException se houver bytes que não sejam UTF-8 válido
        UTF_8.newDecoder().decode(ByteBuffer.wrap(Files.readAllBytes(arquivo)));
    }

    @Test
    public void usuariosJsonEstaEmUtf8EComRegistrosValidos() throws IOException {
        Assume.assumeTrue(Files.exists(USUARIOS));
        estrito(USUARIOS);

        List<Usuario> usuarios = JsonUtil.ler(USUARIOS.toString(), Usuario.class);
        assertFalse("usuarios.json não pode estar vazio", usuarios.isEmpty());

        Set<String> emails = new HashSet<>();
        boolean temAdmin = false;
        for (Usuario u : usuarios) {
            assertTrue("Tipo inesperado: " + u, u instanceof admin || u instanceof inquilino);
            // refaz a validação dos setters, que o Gson ignora ao ler o arquivo
            new Usuario(u.getNome(), u.getEmail(), u.getSenha(), u instanceof admin ? "admin" : "inquilino");
            assertTrue("Email repetido: " + u.getEmail(), emails.add(u.getEmail().toLowerCase(Locale.ROOT)));
            temAdmin |= u instanceof admin;
        }
        assertTrue("Precisa existir ao menos um administrador", temAdmin);
    }

    @Test
    public void imoveisJsonEstaEmUtf8EComRegistrosValidos() throws IOException {
        Assume.assumeTrue(Files.exists(IMOVEIS));
        estrito(IMOVEIS);

        List<Imovel> imoveis = JsonUtil.ler(IMOVEIS.toString(), Imovel.class);
        assertFalse("imoveis.json não pode estar vazio", imoveis.isEmpty());

        for (Imovel i : imoveis) {
            assertFalse("Acento corrompido em: " + i.getNome(), i.getNome().indexOf('\uFFFD') >= 0);
            assertFalse("Acento corrompido em: " + i.getLocalizacao(), i.getLocalizacao().indexOf('\uFFFD') >= 0);
        }
    }

    @Test
    public void lerESalvarOsBancosReaisNaoMudaOConteudo() throws IOException {
        Assume.assumeTrue(Files.exists(USUARIOS) && Files.exists(IMOVEIS));
        Path temporario = Files.createTempFile("nexus-", ".json");
        try {
            List<Usuario> usuarios = JsonUtil.ler(USUARIOS.toString(), Usuario.class);
            JsonUtil.salvar(temporario.toString(), usuarios);
            assertEquals(Files.readString(USUARIOS, UTF_8).strip(), Files.readString(temporario, UTF_8).strip());

            List<Imovel> imoveis = JsonUtil.ler(IMOVEIS.toString(), Imovel.class);
            JsonUtil.salvar(temporario.toString(), imoveis);
            assertEquals(Files.readString(IMOVEIS, UTF_8).strip(), Files.readString(temporario, UTF_8).strip());
        } finally {
            Files.deleteIfExists(temporario);
        }
    }
}
