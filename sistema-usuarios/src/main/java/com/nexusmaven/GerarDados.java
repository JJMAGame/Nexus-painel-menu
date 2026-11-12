package com.nexusmaven;

import java.util.List;
import java.util.Scanner;

public class GerarDados {

    public static void main(String[] args) {
        try (Scanner scanner = EntradaConsole.criarScanner()) {
            executar(scanner);
        }
    }

    public static void executar(Scanner scanner) {
        executar(scanner, JsonUtil.ARQUIVO_USUARIOS, JsonUtil.ARQUIVO_IMOVEIS);
    }

    static void executar(Scanner scanner, String arquivoUsuarios, String arquivoImoveis) {
        System.out.println("\n⏳ Gerando novos dados...");
        System.out.println("Escolha o tipo de dados que deseja gerar:");
        System.out.println("1. Gerar usuário");
        System.out.println("2. Gerar imóvel");
        System.out.print("Escolha: ");
        
        String escolha = scanner.nextLine().trim();

        // ⚠️ Adicionado o 'break' para evitar que o case "1" caia no case "2"
        switch (escolha) {
            case "1":
                gerarUsuario(scanner, arquivoUsuarios);
                break;
            case "2":
                gerarImovel(scanner, arquivoImoveis);
                break;
            default:
                System.out.println("❌ Opção inválida. Nenhum dado foi gerado.");
                break;
        }
    }

    private static void gerarUsuario(Scanner scanner, String arquivoUsuarios) {
        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();
        
        System.out.print("Digite o email do usuário: ");
        String email = scanner.nextLine();
        
        System.out.print("Digite o CPF do usuário: ");
        String cpf = scanner.nextLine();
        
        System.out.print("Digite a senha (mínimo 6 e máximo 12 caracteres): ");
        String senha = scanner.nextLine();

        System.out.println("\nQual o tipo de conta?");
        System.out.println("1 - Inquilino (Padrão)");
        System.out.println("2 - Administrador");
        System.out.print("Escolha o tipo: ");
        boolean administrador = scanner.nextLine().trim().equals("2");

        try {
            Usuario novo = administrador
                    ? new admin(nome, email, senha, cpf)
                    : new inquilino(nome, email, senha, cpf);

            List<Usuario> usuarios = JsonUtil.ler(arquivoUsuarios, Usuario.class);

            boolean emailJaExiste = usuarios.stream()
                    .anyMatch(u -> novo.getEmail().equalsIgnoreCase(u.getEmail()));
            if (emailJaExiste) {
                System.out.println("❌ Erro: já existe um usuário com o email " + novo.getEmail() + ".");
                return;
            }

            usuarios.add(novo);
            JsonUtil.salvar(arquivoUsuarios, usuarios);

            String tipoExibicao = administrador ? "Administrador" : "Inquilino";
            System.out.println("✅ Usuário do tipo " + tipoExibicao + " salvo com sucesso!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void gerarImovel(Scanner scanner, String arquivoImoveis) {
        System.out.print("Digite o nome do imóvel: ");
        String nome = scanner.nextLine();
        
        System.out.print("Digite a localização do imóvel: ");
        String localizacao = scanner.nextLine();
        
        System.out.print("Digite o CPF do proprietário: ");
        String cpfProprietario = scanner.nextLine();

        System.out.print("Digite o valor do aluguel (ex: 1500,50): ");
        double valorAluguel;
        try {
            // ⚠️ Usando a classe Moeda para ler valores com vírgula ou ponto
            valorAluguel = Moeda.ler(scanner.nextLine()); 
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
            return;
        }

        try {
            Imovel imovel = new Imovel(nome, localizacao, cpfProprietario, valorAluguel);

            List<Imovel> imoveis = JsonUtil.ler(arquivoImoveis, Imovel.class);
            imoveis.add(imovel);
            JsonUtil.salvar(arquivoImoveis, imoveis);

            // ⚠️ Usando a classe Moeda para formatar o valor bonitinho (R$ 1.500,50)
            System.out.println("✅ Imóvel salvo! Aluguel: " + Moeda.formatar(valorAluguel));
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }
}
