package app;

import dao.ClienteDAO;
import dao.FilmeDAO;
import model.Cliente;
import model.Filme;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int opcao;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("\n========= MENU  =========");
            System.out.println("1 - Gerenciar clientes");
            System.out.println("2 - Gerenciar filmes");
            System.out.println("3 - Gerenciar aluguéis");
            System.out.println("0 - Sair");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    menuGerenciarCliente(sc);
                    break;
                case 2:
                    menuGerenciarFilme(sc);
                    break;
                case 3:
                    break;
                case 0:
                    System.out.println("\nSaindo...");
                    break;
                default:
                    System.err.println("Opção inválida.\n");
            }
        }while(opcao != 0);
    }

    public static int lerInt(Scanner sc, String mensagem){
        while(true){
            try {
                System.out.print(mensagem);
                return Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException e){
                System.out.println("\nOpção inválida. Por favor digite um número inteiro.");
            }
        }
    }

    public static String lerString(Scanner sc, String mensagem){
        while(true){
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();

            if (!entrada.isEmpty()) {
                return entrada;
            }

            System.out.println("Entrada inválida. Por favor, digite um valor não vazio.\n");
        }
    }

    public static LocalDate lerData(Scanner sc, String mensagem){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true){
            try {
                System.out.print(mensagem);
                String data = sc.nextLine().trim();
                return LocalDate.parse(data, formatter);
            }catch (DateTimeParseException e){
                System.out.println("\nData inválida! Por favor, digite no formato dd/MM/yyyy.\n");            }
        }
    }

    public static String lerTelefone(Scanner sc, String mensagem){
        String regex = "^\\(\\d{2}\\)\\s?\\d{5}-\\d{4}$";

        while(true){
            System.out.print(mensagem);
            String telefone = sc.nextLine().trim();

            if(!telefone.matches(regex)){
                System.out.println("\nTelefone inválido! Tente novamente.\n");
                continue;
            }

            return telefone;
        }
    }

    public static boolean lerDisponibilidade(Scanner sc, String mensagem){
        while(true){
            try {
                System.out.print(mensagem);
                int opcao = Integer.parseInt(sc.nextLine());

                if(opcao == 1){
                    return true;
                } else if(opcao == 2){
                    return false;
                } else {
                    System.out.println("\nOpção inválida. Digite 1 para SIM ou 2 para NÃO.\n");
                }
            }catch (NumberFormatException e){
                System.out.println("\nEntrada inválida. Por favor, digite apenas números (1 para SIM ou 2 para NÃO).\n");
            }
        }
    }

    public static void menuGerenciarCliente(Scanner sc){
        int opcao;
        ClienteDAO clienteDAO = new ClienteDAO();

        do{
            System.out.println("\n====== GERENCIAR CLIENTES ======");
            System.out.println("1 - Cadastrar novo cliente");
            System.out.println("2 - Listar todos os clientes");
            System.out.println("3 - Buscar cliente por ID");
            System.out.println("4 - Atualizar dados do cliente");
            System.out.println("5 - Remover cliente");
            System.out.println("0 - Voltar");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    String nome = lerString(sc, "\nInforme o nome do cliente: ");
                    String email = lerString(sc, "Informe o Email do cliente: ");
                    LocalDate dataNascimento = lerData(sc, "Informe a data de nascimento (dd/MM/yyyy): ");
                    String telefone = lerTelefone(sc, "Informe o telefone no formato (11)91234-5678: ");

                    Cliente c = new Cliente(nome, email, dataNascimento, telefone);
                    clienteDAO.inserir(c);
                    break;
                case 2:
                    List<Cliente> clientes = clienteDAO.listar();
                    if(clientes.isEmpty()){
                        System.err.println("\nNão há clientes cadastrados!");
                        continue;
                    }
                    clientes.forEach(System.out::println);
                    System.out.println("===================================");
                    break;
                case 3:
                    int idCliente = lerInt(sc, "\nInforme o ID do cliente: ");
                    Cliente clienteEncontrado = clienteDAO.buscarPorId(idCliente);
                    if(clienteEncontrado != null){
                        System.out.println(clienteEncontrado);
                    }
                    break;
                case 4:
                    List<Cliente> lista = clienteDAO.listar();
                    if(lista.isEmpty()){
                        System.err.println("\nNão há clientes cadastrados!");
                        continue;
                    }
                    lista.forEach(System.out::println);
                    int id = lerInt(sc, "\nInforme o ID do cliente que deseja atualizar: ");
                    Cliente clienteExistente = clienteDAO.buscarPorId(id);

                    if(clienteExistente == null){
                        System.err.println("\nNenhum cliente encontrado com esse ID.");
                        continue;
                    }

                    String novoNome = lerString(sc, "Informe o Nome do cliente: ");
                    String novoEmail = lerString(sc, "Informe o Email do cliente: ");
                    LocalDate novaDataNascimento = lerData(sc, "Informe a Data de Nascimento: ");
                    String novoTelefone = lerTelefone(sc, "Informe o telefone no formato (11)91234-5678: ");

                    clienteDAO.atualizar(new Cliente(id, novoNome, novoEmail, novaDataNascimento, novoTelefone));
                    break;
                case 5:
                    int idParaRemover = lerInt(sc, "\nInforme o ID do cliente que deseja remover: ");
                    clienteDAO.remover(idParaRemover);
                    break;
                case 0:
                    break;
                default:
                    System.err.println("Opção inválida.\n");
            }
        }while(opcao != 0);
    }

    public static void menuGerenciarFilme(Scanner sc){
        FilmeDAO filmeDAO = new FilmeDAO();
        int opcao;

        do{
            System.out.println("\n====== GERENCIAR FILMES ======");
            System.out.println("1 - Cadastrar novo filme");
            System.out.println("2 - Listar todos os filmes");
            System.out.println("3 - Buscar filme por ID");
            System.out.println("4 - Atualizar dados do filme");
            System.out.println("5 - Remover filme");
            System.out.println("0 - Voltar");
            opcao = lerInt(sc, "Escolha uma opção: ");

            switch (opcao){
                case 1:
                    String nome = lerString(sc, "\nInforme o nome do filme: ");
                    String genero = lerString(sc, "Informe o gênero do filme: ");
                    LocalDate dataLancamento = lerData(sc, "Informe a data de lançamento: ");
                    boolean disponivel = lerDisponibilidade(sc, "O filme está disponível? \n1 - Sim\n2 - Não\nEscolha um número: ");
                    Filme filme = new Filme(nome, genero, dataLancamento, disponivel);
                    filmeDAO.inserir(filme);
                    break;
                case 2:
                    List<Filme> filmes = filmeDAO.listar();
                    if(filmes.isEmpty()){
                        System.out.println("\nNão há filmes cadastrados!");
                        continue;
                    }
                    filmes.forEach(System.out::println);
                    System.out.println("===================================");
                    break;
                case 3:
                    int id = lerInt(sc, "\nInforme o ID do filme: ");
                    Filme filmeEncontrado = filmeDAO.buscarPorId(id);
                    if(filmeEncontrado != null){
                        System.out.println(filmeEncontrado);
                    }
                    break;
                case 4:
                    List<Filme> lista = filmeDAO.listar();
                    if(lista.isEmpty()){
                        System.out.println("\nNão há filmes cadastrados!");
                        continue;
                    }
                    lista.forEach(System.out::println);
                    System.out.println("===================================");
                    int idFilme = lerInt(sc, "\nInforme o ID do filme que deseja atualizar: ");
                    Filme f = filmeDAO.buscarPorId(idFilme);

                    if(f == null){
                        continue;
                    }

                    String novoNome = lerString(sc, "Informe o nome do filme: ");
                    String novoGenero = lerString(sc, "Informe o gênero do filme: ");
                    LocalDate novaDataLancamento = lerData(sc, "Informe a data de lançamento: ");
                    boolean novoDisponivel = lerDisponibilidade(sc, "O filme está disponível? \n1 - Sim\n2 - Não\nEscolha um número: ");

                    f = new Filme(idFilme, novoNome, novoGenero, novaDataLancamento, novoDisponivel);
                    filmeDAO.atualizar(f);
                    break;
                case 5:
                    int idParaRemover = lerInt(sc, "\nInforme o ID do filme que deseja remover: ");
                    filmeDAO.remover(idParaRemover);
                    break;
                case 0:
                    break;
                default:
                    System.err.println("Opção inválida.\n");
            }
        }while(opcao != 0);
    }

}
