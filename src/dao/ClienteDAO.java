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
        if(cliente == null
        || cliente.getNome() == null || cliente.getNome().trim().isEmpty()
        || cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()
        || cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()
        || cliente.getDataNascimento() == null || cliente.getDataNascimento().isAfter(LocalDate.now())){
            System.err.println("Dados inválidos do cliente.");
            return;
        }

        String sql = "INSERT INTO cliente (nome, email, data_nascimento, telefone) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);

            stmt.setString(1, cliente.getNome().trim());
            stmt.setString(2, cliente.getEmail().trim());
            stmt.setDate(3, java.sql.Date.valueOf(cliente.getDataNascimento()));
            stmt.setString(4, cliente.getTelefone().trim());

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
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String telefone = rs.getString("telefone");

                clientes.add(new Cliente(id, nome, email, dataNascimento, telefone));
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

                    return new Cliente(idCliente, nome, email, dataNascimento, telefone);
                } else {
                    System.err.println("\nNenhum cliente encontrado com o ID (" + id + ").");
                    return null;
                }
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao buscar cliente por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Cliente cliente){
        if(cliente == null
        || cliente.getNome() == null || cliente.getNome().trim().isEmpty()
        || cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()
        || cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()
        || cliente.getDataNascimento() == null || cliente.getDataNascimento().isAfter(LocalDate.now()))
        {
            System.err.println("\nDados inválidos do cliente.");
            return;
        }

        String selectSql = "SELECT id, nome, email, data_nascimento, telefone " +
                "FROM cliente WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(selectSql)){

            connection.setAutoCommit(false);
            stmt.setInt(1, cliente.getId());

            try (ResultSet rs = stmt.executeQuery()){
                if(!rs.next()){
                    System.err.println("\nNenhum cliente encontrado com o ID (" + cliente.getId() + ").");
                    connection.rollback();
                    return;
                }

                String updateSql = "UPDATE cliente SET nome = ?, email = ?, " +
                        "data_nascimento = ?, telefone = ? " +
                        "WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)){

                    updateStmt.setString(1, cliente.getNome().trim());
                    updateStmt.setString(2, cliente.getEmail().trim());
                    updateStmt.setDate(3, java.sql.Date.valueOf(cliente.getDataNascimento()));
                    updateStmt.setString(4, cliente.getTelefone().trim());
                    updateStmt.setInt(5, cliente.getId());

                    int rows = updateStmt.executeUpdate();

                    if(rows > 0){
                        System.out.println("\nCliente atualizado com sucesso!");
                        connection.commit();
                    } else {
                        System.err.println("\nFalha ao atualizar cliente.");
                        connection.rollback();
                    }

                }
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao atualizar cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void remover(int id){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return;
        }

        try (Connection connection = DB.getConnection()){
            connection.setAutoCommit(false);

            AluguelDAO aluguelDAO = new AluguelDAO();
            boolean existeAluguel = aluguelDAO.existeAluguelPorClienteId(id, connection);

            if(existeAluguel){
                System.err.println("\nNão foi possível excluir o cliente: existem aluguéis vinculados.");
                connection.rollback();
                return;
            }

            String sql = "DELETE FROM cliente WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();

                if(rows > 0){
                    System.out.println("\nCliente excluído com sucesso!");
                    connection.commit();
                } else {
                    System.err.println("\nNenhum cliente com o ID (" + id + ") foi excluido.");
                    connection.rollback();
                }
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao excluir cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
