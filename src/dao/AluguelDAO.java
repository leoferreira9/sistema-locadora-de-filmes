package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
