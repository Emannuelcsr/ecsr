package teste;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestJDBCConnection {
    public static void main(String[] args) {
        // Configuração da conexão ao banco de dados
        String url = "jdbc:postgresql://localhost:5432/ecsr"; // Banco de dados 'ecsr'
        String user = "ecsr"; // Usuário dentro do banco 'ecsr' que tem permissão de acesso
        String password = "admin"; // Senha do usuário no banco 'ecsr'

        // Conexão com o banco de dados
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Conexão bem-sucedida ao banco de dados!");

                // Query para verificar se o usuário admin existe na tabela entidades
                String query = "SELECT ent_login, ent_senha FROM entidade WHERE ent_login = ? AND ent_senha = ?";
                
                // Substitua "admin" e "123" pelos valores desejados para login e senha
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, "alex");  // Login que você quer testar
                    statement.setString(2, "alex");    // Senha correspondente

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            System.out.println("Usuário encontrado: " + resultSet.getString("ent_login"));
                        } else {
                            System.out.println("Usuário ou senha inválidos.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        }
    }
}