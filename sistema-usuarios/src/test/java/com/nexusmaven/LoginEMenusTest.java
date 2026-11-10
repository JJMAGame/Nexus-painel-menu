package com.nexusmaven;

import static com.nexusmaven.ApoioDeTestes.capturarSaida;
import static com.nexusmaven.ApoioDeTestes.entrada;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** Login (Main) e menus (Admin e Inquilino), com teclado simulado e banco temporário. */
public class LoginEMenusTest {

    @Rule
    public TemporaryFolder pasta = new TemporaryFolder();

    private String banco;

    @Before
    public void criarBanco() {
        banco = new File(pasta.getRoot(), "usuarios.json").getPath();
        JsonUtil.salvar(banco, List.of(
                new admin("Chefe", "admin@nexus.com", "admin123", ""),
                new inquilino("João", "joao@nexus.com", "comum123", "")));
    }

    private String loginComo(String... digitado) {
        return capturarSaida(() -> main.executar(entrada(digitado), banco));
    }

    // ===== Login =====

    @Test
    public void loginDeInquilinoAbreOMenuDoInquilino() {
        String saida = loginComo("joao@nexus.com", "comum123", "0");
        assertTrue(saida, saida.contains("Login realizado com sucesso! Bem-vindo, João."));
        assertTrue(saida, saida.contains("MENU INQUILINO"));
        assertFalse(saida, saida.contains("MENU ADMIN"));
    }

    @Test
    public void loginDeAdminAbreOMenuDoAdmin() {
        String saida = loginComo("ADMIN@nexus.com", "admin123", "0");
        assertTrue(saida, saida.contains("MENU ADMIN"));
        assertFalse(saida, saida.contains("MENU INQUILINO"));
    }

    @Test
    public void senhaIncorretaPedeDeNovoEDepoisAceita() {
        String saida = loginComo("joao@nexus.com", "errada1", "joao@nexus.com", "comum123", "0");
        assertTrue(saida, saida.contains("Senha incorreta"));
        assertTrue(saida, saida.contains("Login realizado com sucesso"));
    }

    @Test
    public void emailInexistenteEFimDaEntradaEncerramSemTravar() {
        String saida = loginComo("ninguem@nexus.com", "123456");
        assertTrue(saida, saida.contains("Email não encontrado"));
        assertFalse(saida, saida.contains("MENU"));
    }

    @Test
    public void bancoVazioAvisaParaRodarOGerarDados() {
        String vazio = new File(pasta.getRoot(), "nao-existe.json").getPath();
        String saida = capturarSaida(() -> main.executar(entrada("a", "b"), vazio));
        assertTrue(saida, saida.contains("Nenhum usuário encontrado"));
    }

    // ===== Menu do inquilino =====

    private String menuInquilino(String... digitado) {
        inquilino inquilino = new inquilino("João", "joao@nexus.com", "comum123", "");
        return capturarSaida(() -> inquilino.abrirMenu(entrada(digitado)));
    }

    @Test
    public void inquilinoVeSeusDados() {
        String saida = menuInquilino("1", "0");
        assertTrue(saida, saida.contains("Nome: João"));
        assertTrue(saida, saida.contains("Email: joao@nexus.com"));
    }

    @Test
    public void opcoesAindaNaoImplementadasAvisamEmDesenvolvimento() {
        String saida = menuInquilino("3", "4", "5", "6", "7", "0");
        for (String recurso : new String[] {"Meu contrato", "Próximo aluguel", "Extratos", "Documentos", "Notificações"}) {
            assertTrue(recurso + " / " + saida, saida.contains(recurso + " - funcionalidade em desenvolvimento."));
        }
    }

    @Test
    public void menuDoInquilinoTrataTextoOpcaoInvalidaEFimDaEntrada() {
        String saida = menuInquilino("abc", "99");   // sem "0": a entrada termina e o menu sai sozinho
        assertTrue(saida, saida.contains("Por favor, digite um número válido"));
        assertTrue(saida, saida.contains("Opção inválida"));
        assertTrue(saida, saida.contains("Saindo do menu do inquilino"));
    }

    // ===== Menu do admin =====

    @Test
    public void menuDoAdminTrataOpcoesESai() {
        admin admin = new admin("Chefe", "admin@nexus.com", "admin123", "");
        String saida = capturarSaida(() -> admin.abrirMenu(entrada("2", "x", "7", "0")));
        assertTrue(saida, saida.contains("Registrar pagamentos - funcionalidade em desenvolvimento."));
        assertTrue(saida, saida.contains("Por favor, digite um número válido"));
        assertTrue(saida, saida.contains("Opção inválida"));
        assertTrue(saida, saida.contains("Saindo do menu admin"));
    }

    @Test
    public void adminGeraDadosPeloMenuUsandoOMesmoScannerDoPrograma() {
        // Opção 1 do menu chama o GerarDados; depois o menu continua lendo do mesmo teclado.
        // Só a opção inválida é digitada dentro do GerarDados, então nenhum banco é alterado.
        admin admin = new admin("Chefe", "admin@nexus.com", "admin123", "");
        String saida = capturarSaida(() -> admin.abrirMenu(entrada("1", "9", "0")));
        assertTrue(saida, saida.contains("Nenhum dado foi gerado"));   // GerarDados recebeu o "9"
        assertTrue(saida, saida.contains("Saindo do menu admin"));     // e o menu ainda leu o "0"
    }
}
