package dao;

import db.DB;
import model.Aluguel;
import model.Cliente;
import model.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AluguelDAO {
    public boolean existeAluguelPorClienteId(int id, Connection connection){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return false;
        }

        String sql = "SELECT id, cliente_id, filme_id, data_inicio, data_fim " +
                "FROM aluguel WHERE cliente_id = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                return rs.next();
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao buscar aluguel para o cliente de ID: " + id);
            throw new RuntimeException(e);
        }
    }

    public boolean existeAluguelPorFilmeId(int id, Connection connection){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return false;
        }

        String sql = "SELECT id FROM aluguel WHERE filme_id = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                return rs.next();
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao buscar aluguel para o filme de ID: " + id);
            throw new RuntimeException(e);
        }
    }

    public void cadastrarAluguel(Aluguel aluguel){
        if(aluguel == null ||
           aluguel.getCliente() == null || aluguel.getCliente().getId() <= 0 ||
           aluguel.getFilme() == null || aluguel.getFilme().getId() <= 0 ||
           aluguel.getDataInicio() == null || aluguel.getDataFim() == null ||
           aluguel.getDataInicio().isAfter(aluguel.getDataFim())){

           System.err.println("\nDados de aluguel inválidos.");
           return;
        }

        String sql = "INSERT INTO aluguel (cliente_id, filme_id, data_inicio, data_fim) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);

            stmt.setInt(1, aluguel.getCliente().getId());
            stmt.setInt(2, aluguel.getFilme().getId());
            stmt.setDate(3, java.sql.Date.valueOf(aluguel.getDataInicio()));
            stmt.setDate(4, java.sql.Date.valueOf(aluguel.getDataFim()));

            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\nAluguel cadastrado com sucesso!");
                connection.commit();
            } else {
                System.err.println("\nNenhum aluguel foi cadastrado.");
                connection.rollback();
            }
        }catch (SQLException e){
            System.err.println("\nErro no banco ao cadastrar aluguel: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Aluguel> listar(){

        String sql = "SELECT id, cliente_id, filme_id, data_inicio, data_fim " +
                "FROM aluguel";

        List<Aluguel> aluguels = new ArrayList<>();

        ClienteDAO clienteDAO = new ClienteDAO();
        FilmeDAO filmeDAO = new FilmeDAO();

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                Cliente c = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
                Filme f = filmeDAO.buscarPorId(rs.getInt("filme_id"));

                if(c == null || f == null){
                    System.err.println("\nAluguel ignorado: cliente ou filme não encontrado.");
                    continue;
                }

                LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim").toLocalDate();

                Aluguel aluguel = new Aluguel(c, f, dataInicio, dataFim);
                aluguels.add(aluguel);
            }
            return aluguels;
        }catch (SQLException e){
            System.err.println("\nErro ao listar alugueis: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Aluguel aluguel){
        if(aluguel == null ||
           aluguel.getCliente() == null || aluguel.getCliente().getId() <= 0 ||
           aluguel.getFilme() == null || aluguel.getFilme().getId() <= 0 ||
           aluguel.getDataInicio() == null || aluguel.getDataFim() == null ||
           aluguel.getDataInicio().isAfter(aluguel.getDataFim())){

           System.err.println("\nDados de aluguel inválidos.");
           return;
        }

        String sql = "SELECT id, cliente_id, filme_id, data_inicio, data_fim " +
                "FROM aluguel WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);
            stmt.setInt(1, aluguel.getId());

            try (ResultSet rs = stmt.executeQuery()){
                if(!rs.next()){
                    System.err.println("\nNenhum aluguel encontrado com o ID " + aluguel.getId());
                    connection.rollback();
                    return;
                }

                String updateSql = "UPDATE aluguel SET cliente_id = ?, filme_id = ?, data_inicio = ?, data_fim = ? " +
                        "WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)){
                    updateStmt.setInt(1, aluguel.getCliente().getId());
                    updateStmt.setInt(2, aluguel.getFilme().getId());
                    updateStmt.setDate(3, java.sql.Date.valueOf(aluguel.getDataInicio()));
                    updateStmt.setDate(4, java.sql.Date.valueOf(aluguel.getDataFim()));
                    updateStmt.setInt(5, aluguel.getId());

                    int rows = updateStmt.executeUpdate();

                    if(rows > 0){
                        System.out.println("\nAluguel atualizado com sucesso!");
                        connection.commit();
                    } else {
                        System.err.println("\nFalha ao atualizar aluguel.");
                        connection.rollback();
                    }
                }
            }
        }catch (SQLException e){
            System.err.println("\nErro no banco ao atualizar aluguel: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void remover(int id){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return;
        }

        String sql = "DELETE FROM aluguel WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\nAluguel excluído com sucesso!");
            } else {
                System.err.println("\nNenhum aluguel encontrado com o ID " + id);
            }
        }catch (SQLException e){
            System.err.println("\nErro no banco ao excluir aluguel: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
