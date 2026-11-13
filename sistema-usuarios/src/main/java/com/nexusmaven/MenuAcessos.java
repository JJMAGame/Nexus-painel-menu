package com.nexusmaven;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Acessos ao sistema: funcionários da Fundação e inquilinos.
 * O cadastro de um novo acesso reaproveita o GerarDados, sem duplicar a regra de criação.
 *
 * @author Melqui
 */
public class MenuAcessos {

    private final Scanner scanner;
    private final String arquivoUsuarios;
    private final String emailLogado;

    public MenuAcessos(Scanner scanner, String emailLogado) {
        this(scanner, JsonUtil.ARQUIVO_USUARIOS, emailLogado);
    }

    MenuAcessos(Scanner scanner, String arquivoUsuarios, String emailLogado) {
        this.scanner = scanner;
        this.arquivoUsuarios = arquivoUsuarios;
        this.emailLogado = emailLogado;
    }

    public void abrir() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------ ACESSOS ------");
            System.out.println("1 - Funcionários da Fundação");
            System.out.println("2 - Inquilinos");
            System.out.println("3 - Buscar por nome, e-mail ou CPF");
            System.out.println("4 - Cadastrar novo acesso");
            System.out.println("5 - Remover acesso");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = EntradaConsole.lerOpcao(scanner);
            try {
                switch (opcao) {
                    case 1: listar("FUNCIONÁRIOS", filtrar(true)); break;
                    case 2: listar("INQUILINOS", filtrar(false)); break;
                    case 3: buscar(); break;
                    case 4: GerarDados.executar(scanner); break;
                    case 5: remover(); break;
                    case 0: break;
                    default: System.out.println("❌ Opção inválida."); break;
                }
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    /** Funcionário é toda conta que não é de inquilino (hoje, os administradores). */
    static boolean ehFuncionario(Usuario u) {
        return !(u instanceof inquilino);
    }

    static String perfil(Usuario u) {
        if (u instanceof admin) return "Administrador";
        if (u instanceof inquilino) return "Inquilino";
        return "Funcionário";
    }

    /** Mostra só o meio do CPF, como em documentos impressos: ***.456.789-** */
    static String mascararCpf(String cpf) {
        String digitos = cpf == null ? "" : cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) return cpf == null ? "" : cpf;
        return "***." + digitos.substring(3, 6) + "." + digitos.substring(6, 9) + "-**";
    }

    private List<Usuario> filtrar(boolean funcionarios) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : JsonUtil.ler(arquivoUsuarios, Usuario.class)) {
            if (ehFuncionario(u) == funcionarios) resultado.add(u);
        }
        return resultado;
    }

    private void buscar() {
        String termo = EntradaTexto.perguntar(scanner, "Digite parte do nome, e-mail ou CPF: ");
        if (termo == null) return;
        String procurado = termo.toLowerCase(Locale.ROOT);
        String soDigitos = termo.replaceAll("\\D", "");
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : JsonUtil.ler(arquivoUsuarios, Usuario.class)) {
            boolean achou = u.getNome().toLowerCase(Locale.ROOT).contains(procurado)
                    || u.getEmail().contains(procurado)
                    || (!soDigitos.isEmpty() && u.getCpf() != null && u.getCpf().replaceAll("\\D", "").contains(soDigitos));
            if (achou) resultado.add(u);
        }
        listar("BUSCA: \"" + termo + "\"", resultado);
    }

    void listar(String titulo, List<Usuario> lista) {
        System.out.println("\n--- ACESSOS: " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhum acesso encontrado.");
            return;
        }
        String formato = "%-3s %-24s %-30s %-15s %s%n";
        System.out.printf(formato, "#", "Nome", "E-mail", "CPF", "Perfil");
        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);
            String voce = u.getEmail().equalsIgnoreCase(emailLogado) ? " (você)" : "";
            System.out.printf(formato, i + 1, EntradaTexto.cortar(u.getNome(), 24), EntradaTexto.cortar(u.getEmail(), 30),
                    mascararCpf(u.getCpf()), perfil(u) + voce);
        }
        System.out.println("-------------------------------------------");
        System.out.println(lista.size() + " acesso(s)");
    }

    void remover() {
        List<Usuario> usuarios = JsonUtil.ler(arquivoUsuarios, Usuario.class);
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum acesso cadastrado.");
            return;
        }
        int i = EntradaTexto.escolher(scanner, "\nQual acesso remover?", usuarios,
                u -> u.getNome() + " | " + u.getEmail() + " | " + perfil(u));
        if (i < 0) return;
        Usuario alvo = usuarios.get(i);

        if (alvo.getEmail().equalsIgnoreCase(emailLogado)) {
            System.out.println("❌ Você não pode remover o seu próprio acesso.");
            return;
        }
        long administradores = usuarios.stream().filter(u -> u instanceof admin).count();
        if (alvo instanceof admin && administradores <= 1) {
            System.out.println("❌ O sistema precisa de pelo menos um Administrador.");
            return;
        }
        if (!EntradaTexto.confirmar(scanner, "Remover o acesso de " + alvo.getNome() + "? Esta ação não pode ser desfeita")) {
            System.out.println("Nada foi alterado.");
            return;
        }
        usuarios.remove(i);
        JsonUtil.salvar(arquivoUsuarios, usuarios);
        System.out.println("✅ Acesso de " + alvo.getNome() + " removido.");
    }
}
