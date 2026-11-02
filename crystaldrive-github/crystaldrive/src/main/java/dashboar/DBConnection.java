package dashboar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/location_vehicules?useSSL=false&serverTimezone=UTC"; // Changez l'URL de votre DB
    private static final String USER = "root"; // Utilisateur de la base de données
    private static final String PASSWORD = ""; // Mot de passe de la base de données

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
