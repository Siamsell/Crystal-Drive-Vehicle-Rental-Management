package dashboar;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RetourDAO {

    // Informations de connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/Location_vehicules";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Méthode pour établir la connexion
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Ajouter un retour
    public void ajouterRetour(Retour retour) {
        String sql = "INSERT INTO retour (id_reservation, date_retour, etat_retour, frais_supplementaires) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, retour.getIdReservation());
            statement.setDate(2, new Date(retour.getDateRetour().getTime()));
            statement.setString(3, retour.getEtatRetour());
            statement.setBigDecimal(4, retour.getFraisSupplementaires());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mise à jour de l'état d'un véhicule
    public void mettreAJourEtat(int idReservation, String nouvelEtat) {
        // La requête SQL met à jour l'état du véhicule en fonction de l'ID de la réservation
        String sql = "UPDATE vehicule v " +
                "JOIN Reservation r ON v.id_vehicule = r.id_vehicule " +
                "SET v.etat = ? " +
                "WHERE r.id_reservation = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nouvelEtat); // Nouveau statut ("Disponible" ou "Maintenance")
            statement.setInt(2, idReservation); // Identifiant de réservation

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("État du véhicule mis à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez l'ID de réservation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer tous les retours
    public List<Retour> getRetours() {
        List<Retour> retours = new ArrayList<>();
        String sql = "SELECT * FROM retour";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int idRetour = resultSet.getInt("id_retour");
                int idReservation = resultSet.getInt("id_reservation");
                Date dateRetour = resultSet.getDate("date_retour");
                String etatRetour = resultSet.getString("etat_retour");
                BigDecimal fraisSupplementaires = resultSet.getBigDecimal("frais_supplementaires");

                retours.add(new Retour(idRetour, idReservation, dateRetour, etatRetour, fraisSupplementaires));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retours;
    }

    // Récupérer un retour par ID réservation
    public Retour getRetourById(int idReservation) {
        String sql = "SELECT * FROM retour WHERE id_reservation = ?";
        Retour retour = null;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idReservation);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int idRetour = resultSet.getInt("id_retour");
                    Date dateRetour = resultSet.getDate("date_retour");
                    String etatRetour = resultSet.getString("etat_retour");
                    BigDecimal fraisSupplementaires = resultSet.getBigDecimal("frais_supplementaires");

                    retour = new Retour(idRetour, idReservation, dateRetour, etatRetour, fraisSupplementaires);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retour;
    }
}
