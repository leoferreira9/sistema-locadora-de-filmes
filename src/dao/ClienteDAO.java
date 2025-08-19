package dao;

import db.DB;
import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

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
            throw new RuntimeException(e);
        }
    }
}
