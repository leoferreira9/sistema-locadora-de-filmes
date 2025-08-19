package dao;

import db.DB;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente cliente){
        if(cliente.getEmail().isEmpty() || cliente.getEmail() == null
        || cliente.getNome().isEmpty() || cliente.getNome() == null
        || cliente.getTelefone().isEmpty() || cliente.getTelefone() == null
        || cliente.getDataNascimento() == null || cliente.getDataNascimento().isAfter(LocalDate.now())){
            System.err.println("Dados inválidos do cliente.");
            return;
        }

        String sql = "INSERT INTO cliente (nome, email, data_nascimento, telefone) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(cliente.getDataNascimento()));
            stmt.setString(4, cliente.getTelefone());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("\nCliente adicionado com sucesso!");
            } else {
                System.err.println("\nNão foi possível adicionar um novo cliente.");
                connection.rollback();
            }

            connection.commit();
        }catch (SQLException e){
            System.err.println("\nFalha ao adicionar um novo cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> listar(){
        String sql = "SELECT id, nome, email, data_nascimento, telefone " +
                "FROM cliente";

        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            boolean encontrou = false;

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    encontrou = true;

                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String telefone = rs.getString("telefone");

                    clientes.add(new Cliente(id, nome, email, dataNascimento, telefone));
                }

                if(!encontrou){
                    System.err.println("Não há clientes para exibir.");
                }
            }
            return clientes;
        }catch (SQLException e){
            System.err.println("\nFalha ao listar clientes: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Cliente buscarPorId(int id){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return null;
        }

        String sql = "SELECT id, nome, email, data_nascimento, telefone " +
                "FROM cliente WHERE id = ?";

        Cliente cliente;

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    int idCliente= rs.getInt("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                    String telefone = rs.getString("telefone");

                    cliente = new Cliente(idCliente, nome, email, dataNascimento, telefone);
                } else {
                    System.err.println("\nNenhum cliente encontrado com o ID (" + id + ").");
                    return null;
                }
            }
            return cliente;
        }catch (SQLException e){
            System.err.println("\nFalha ao buscar cliente por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
