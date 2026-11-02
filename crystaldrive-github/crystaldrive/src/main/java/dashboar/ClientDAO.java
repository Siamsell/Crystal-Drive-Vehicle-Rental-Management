package dashboar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ClientDAO {
    // Constantes pour la connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/location_vehicules?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Pilote JDBC chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Pilote JDBC introuvable. Assurez-vous d'avoir ajouté mysql-connector-java au classpath.");
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour obtenir une connexion à la base de données
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Ajouter un client dans la base de données
    public static void ajouterClient(Client client) {
        // Requête pour insérer directement dans la table Client
        String insertionQuery = "INSERT INTO Client (nom, prenom, telephone, email, numero_permis) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            try (PreparedStatement insertionStatement = connection.prepareStatement(insertionQuery)) {
                insertionStatement.setString(1, client.getNom());
                insertionStatement.setString(2, client.getPrenom());
                insertionStatement.setString(3, client.getTelephone());
                insertionStatement.setString(4, client.getEmail());
                insertionStatement.setString(5, client.getPermis());

                int rowsAffected = insertionStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Client enregistré avec succès !");
                    System.out.println("Client enregistré avec succès !");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Rechercher des clients par nom, email ou numéro de permis
    public static List<Client> rechercherClients(String query) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client WHERE nom LIKE ? OR email LIKE ? OR numero_permis LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    clients.add(new Client(
                            resultSet.getInt("id_client"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getString("telephone"),
                            resultSet.getString("email"),
                            resultSet.getString("numero_permis")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de clients : " + e.getMessage());
            e.printStackTrace();
        }
        return clients;
    }

    // Récupérer tous les clients
    public static List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM Client";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getInt("id_client"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("numero_permis")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
            e.printStackTrace();
        }
        return clients;
    }

    // Rechercher des locations par nom, email ou numéro de permis
    public static List<Location> rechercherLocations(String query) {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT r.id_reservation, CONCAT(v.marque, ' ', v.modele) AS vehicule, r.date_debut, r.date_fin, " +
                "r.montant_total, r.statut " +
                "FROM Reservation r " +
                "JOIN Vehicule v ON r.id_vehicule = v.id_vehicule " +
                "JOIN Client c ON r.id_client = c.id_client " +
                "WHERE c.nom LIKE ? OR c.email LIKE ? OR c.numero_permis LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    locations.add(new Location(
                            resultSet.getInt("id_reservation"),
                            resultSet.getString("vehicule"),
                            resultSet.getDate("date_debut"),
                            resultSet.getDate("date_fin"),
                            resultSet.getBigDecimal("montant_total"),
                            resultSet.getString("statut")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des locations : " + e.getMessage());
            e.printStackTrace();
        }
        return locations;
    }
}
