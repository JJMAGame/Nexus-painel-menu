package com.nexusmaven;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Testa a leitura e a gravação dos "bancos de dados" JSON, sempre em arquivos temporários. */
public class JsonUtilTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    private String caminho(String nome) {
        return new File(pasta.getRoot(), nome).getPath();
    }

    @Test
    public void arquivoInexistenteRetornaListaVazia() {
        assertTrue(JsonUtil.ler(caminho("nao-existe.json"), Usuario.class).isEmpty());
    }

    @Test
    public void arquivoVazioRetornaListaVazia() throws IOException {
        File vazio = pasta.newFile("vazio.json");
        assertTrue(JsonUtil.ler(vazio.getPath(), Usuario.class).isEmpty());
    }

    @Test
    public void usuariosSobrevivemAoRoundTripComOTipoCorreto() {
        List<Usuario> origem = new ArrayList<>();
        origem.add(new Admin("Ana", "ana@nexus.com", "senha123"));
        origem.add(new Inquilino("João", "joao@nexus.com", "senha456"));
        String arquivo = caminho("usuarios.json");

        JsonUtil.salvar(arquivo, origem);
        List<Usuario> lidos = JsonUtil.ler(arquivo, Usuario.class);

        assertEquals(2, lidos.size());
        assertTrue(lidos.get(0) instanceof Admin);
        assertTrue(lidos.get(1) instanceof Inquilino);
        assertEquals("ana@nexus.com", lidos.get(0).getEmail());
        assertEquals("João", lidos.get(1).getNome());
        assertTrue(lidos.get(1).autenticar("JOAO@nexus.com", "senha456"));
    }

    @Test
    public void registroSemTipoViraInquilino() throws IOException {
        File antigo = pasta.newFile("antigo.json");
        Files.writeString(antigo.toPath(),
                "[{\"nome\":\"Carlos\",\"email\":\"carlos@hotmail.com\",\"senha\":\"123456\"}]", UTF_8);

        List<Usuario> lidos = JsonUtil.ler(antigo.getPath(), Usuario.class);

        assertEquals(1, lidos.size());
        assertTrue(lidos.get(0) instanceof Inquilino);
    }

    @Test
    public void tipoDesconhecidoDaErroClaroEmVezDeStackOverflow() throws IOException {
        File arquivo = pasta.newFile("tipo-estranho.json");
        Files.writeString(arquivo.toPath(),
                "[{\"tipo\":\"visitante\",\"nome\":\"X\",\"email\":\"x@x.com\",\"senha\":\"123456\"}]", UTF_8);
        try {
            JsonUtil.ler(arquivo.getPath(), Usuario.class);
            fail("Era esperado um erro para o tipo desconhecido");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("visitante"));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void jsonComSintaxeQuebradaDaErroClaro() throws IOException {
        File quebrado = pasta.newFile("quebrado.json");
        Files.writeString(quebrado.toPath(), "[{\"nome\": ", UTF_8);
        JsonUtil.ler(quebrado.getPath(), Usuario.class);
    }

    @Test
    public void acentosEApostrofosSaoGravadosEmUtf8EVoltamIntactos() throws IOException {
        List<Imovel> origem = List.of(
                new Imovel("Casa de José", "R. Ubiratã, 226 - Alto José do Pinho, Recife - PE"),
                new Imovel("D'Ávila & Cia", "Av. Ali, 22"));
        String arquivo = caminho("imoveis.json");

        JsonUtil.salvar(arquivo, origem);

        // A decodificação estrita falha se o arquivo não for UTF-8 válido
        String texto = UTF_8.newDecoder().decode(ByteBuffer.wrap(Files.readAllBytes(Path.of(arquivo)))).toString();
        assertTrue(texto.contains("José"));
        assertTrue(texto.contains("Ubiratã"));
        assertTrue("& e ' não devem virar \\u0026 / \\u0027", texto.contains("D'Ávila & Cia"));

        List<Imovel> lidos = JsonUtil.ler(arquivo, Imovel.class);
        assertEquals("Casa de José", lidos.get(0).getNome());
        assertEquals("D'Ávila & Cia", lidos.get(1).getNome());
    }

    @Test
    public void arquivoForaDeUtf8ERecusadoEmVezDeTrocarOsAcentosPorInterrogacao() throws IOException {
        File antigo = pasta.newFile("latin1.json");
        Files.write(antigo.toPath(), "[{\"nome\":\"José\",\"localizacao\":\"Rua A\"}]".getBytes(ISO_8859_1));
        try {
            JsonUtil.ler(antigo.getPath(), Imovel.class);
            fail("Era esperado um erro para arquivo fora de UTF-8");
        } catch (UncheckedIOException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("UTF-8"));
        }
    }

    @Test
    public void imovelAntigoComChaveLnomeContinuaLegivelEESalvaComNome() throws IOException {
        File antigo = pasta.newFile("imoveis-antigo.json");
        Files.writeString(antigo.toPath(), "[{\"lnome\":\"Casa A\",\"localizacao\":\"Rua B\"}]", UTF_8);

        List<Imovel> lidos = JsonUtil.ler(antigo.getPath(), Imovel.class);
        assertEquals("Casa A", lidos.get(0).getNome());

        JsonUtil.salvar(antigo.getPath(), lidos);
        String texto = Files.readString(antigo.toPath(), UTF_8);
        assertTrue(texto.contains("\"nome\""));
        assertFalse(texto.contains("lnome"));
    }

    @Test
    public void salvarSubstituiOConteudoAnteriorENaoDeixaTemporarioParaTras() {
        String arquivo = caminho("u.json");
        JsonUtil.salvar(arquivo, List.of(new Inquilino("Um", "um@um.com", "123456"),
                                          new Inquilino("Dois", "dois@dois.com", "123456")));
        JsonUtil.salvar(arquivo, List.of(new Inquilino("Zé", "ze@ze.com", "123456")));

        List<Usuario> lidos = JsonUtil.ler(arquivo, Usuario.class);
        assertEquals(1, lidos.size());
        assertEquals("Zé", lidos.get(0).getNome());
        assertArrayEquals(new String[] {"u.json"}, pasta.getRoot().list());
    }
}
