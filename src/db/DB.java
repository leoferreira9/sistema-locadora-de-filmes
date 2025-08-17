package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    public static Connection getConnection(){
        try {
            Properties prop = getProperties();
            return DriverManager.getConnection(
                    prop.getProperty("db.url"),
                    prop.getProperty("db.user"),
                    prop.getProperty("db.password")
            );
        }catch (SQLException e){
            System.err.println("Falha ao conectar com o Banco de Dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Properties getProperties(){
        try {
            Properties prop = new Properties();
            prop.load(DB.class.getResourceAsStream("/database/db.properties"));
            return prop;
        }catch (IOException e){
            System.err.println("Falha ao encontrar as propriedades do Banco de Dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
