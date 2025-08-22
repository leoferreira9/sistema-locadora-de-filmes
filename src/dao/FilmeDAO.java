package dao;

import db.DB;
import model.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {

    public void inserir(Filme filme){
        if(filme == null ||
           filme.getNome() == null || filme.getNome().isEmpty() ||
           filme.getGenero() == null || filme.getGenero().isEmpty() ||
           filme.getDataLancamento() == null){

           System.err.println("\nDados inválidos.");
           return;
        }

        String sql = "INSERT INTO filme (nome, genero, data_lancamento, disponivel) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);

            stmt.setString(1, filme.getNome().trim());
            stmt.setString(2, filme.getGenero().trim());
            stmt.setDate(3, java.sql.Date.valueOf(filme.getDataLancamento()));
            stmt.setBoolean(4, filme.isDisponivel());

            int rows = stmt.executeUpdate();

            if(rows > 0){
                System.out.println("\nFilme adicionado com sucesso.");
                connection.commit();
            } else {
                System.err.println("\nNenhum filme foi adicionado!");
                connection.rollback();
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao adicionar um novo filme: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Filme> listar(){

        String sql = "SELECT id, nome, genero, data_lancamento, disponivel FROM filme";
        List<Filme> filmes = new ArrayList<>();

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String genero = rs.getString("genero");
                LocalDate dataLancamento = rs.getDate("data_lancamento").toLocalDate();
                boolean disponivel = rs.getBoolean("disponivel");

                Filme filme = new Filme(id, nome, genero, dataLancamento, disponivel);
                filmes.add(filme);
            }

            return filmes;
        }catch (SQLException e){
            System.err.println("\nFalha ao listar filmes: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Filme buscarPorId(int id){
        if(id <= 0){
            System.err.println("\nID inválido.");
            return null;
        }

        String sql = "SELECT id, nome, genero, data_lancamento, disponivel " +
                "FROM filme WHERE id = ?";

        try (Connection connection = DB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    int idFilme = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String genero = rs.getString("genero");
                    LocalDate dataLancamento = rs.getDate("data_lancamento").toLocalDate();
                    boolean disponivel = rs.getBoolean("disponivel");
                    return new Filme(idFilme, nome, genero, dataLancamento, disponivel);
                } else {
                    System.err.println("\nNenhum filme encontrado com o ID (" + id + ").");
                    return null;
                }
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao buscar filme por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Filme filme){
        if(filme == null ||
           filme.getNome() == null || filme.getNome().trim().isEmpty() ||
           filme.getGenero() == null || filme.getGenero().trim().isEmpty() ||
           filme.getDataLancamento() == null){

           System.err.println("\nDados Inválidos.");
           return;
        }

        String sql = "SELECT id, nome, genero, data_lancamento, disponivel " +
                "FROM filme WHERE id = ?";

        try (Connection connection = DB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            connection.setAutoCommit(false);
            stmt.setInt(1, filme.getId());

            try (ResultSet rs = stmt.executeQuery()){
                if(!rs.next()){
                    System.err.println("\nNenhum filme encontrado com o ID (" + filme.getId() + ").");
                    connection.rollback();
                    return;
                }

                String updateSql = "UPDATE filme SET nome = ?, genero = ?, " +
                        "data_lancamento = ?, disponivel = ? WHERE id = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)){

                    updateStmt.setString(1, filme.getNome().trim());
                    updateStmt.setString(2, filme.getGenero().trim());
                    updateStmt.setDate(3, java.sql.Date.valueOf(filme.getDataLancamento()));
                    updateStmt.setBoolean(4, filme.isDisponivel());
                    updateStmt.setInt(5, filme.getId());

                    int rows = updateStmt.executeUpdate();

                    if(rows > 0){
                        System.out.println("\nFilme atualizado com sucesso!");
                        connection.commit();
                    } else {
                        System.err.println("\nFalha ao atualizar filme.");
                        connection.rollback();
                    }


                }
            }
        }catch (SQLException e){
            System.err.println("\nFalha ao atualizar filme: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
