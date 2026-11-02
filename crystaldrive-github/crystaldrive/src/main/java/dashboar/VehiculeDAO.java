package dashboar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculeDAO {

    // Ajouter un véhicule
    public boolean ajouterVehicule(String modele, String marque, int annee, String carburant,
                                   double prixLocationJour, String etat, String type) {
        String query = "INSERT INTO vehicule (modele, marque, annee, carburant, prix_location_jour, etat, type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, modele);
            stmt.setString(2, marque);
            stmt.setInt(3, annee);
            stmt.setString(4, carburant);
            stmt.setDouble(5, prixLocationJour);
            stmt.setString(6, etat);
            stmt.setString(7, type);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean modifierVehicule(int idVehicule, String modele, String marque, int annee,
                                    String carburant, double prixLocationJour, String etat, String type) {
        // Vérifier si le véhicule existe
        Vehicule vehiculeExistant = getVehiculeParId(idVehicule);
        if (vehiculeExistant == null) {
            System.out.println("Aucun véhicule trouvé avec l'ID: " + idVehicule);
            return false; // Le véhicule n'existe pas, on ne modifie pas
        }

        String query = "UPDATE vehicule SET modele = ?, marque = ?, annee = ?, carburant = ?, " +
                "prix_location_jour = ?, etat = ?, type = ? WHERE id_vehicule = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, modele);
            stmt.setString(2, marque);
            stmt.setInt(3, annee);
            stmt.setString(4, carburant);
            stmt.setDouble(5, prixLocationJour);
            stmt.setString(6, etat);
            stmt.setString(7, type);
            stmt.setInt(8, idVehicule);

            int rowsAffected = stmt.executeUpdate(); // Vérifie combien de lignes ont été modifiées
            if (rowsAffected == 0) {
                System.out.println("Aucune ligne modifiée, l'ID du véhicule n'existe peut-être pas.");
                return false; // Aucune ligne modifiée
            }

            // Commit si nécessaire (si autocommit est désactivé)
            conn.commit();

            System.out.println("Véhicule mis à jour avec succès.");
            return true; // Modification réussie

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Supprimer un véhicule
    public boolean supprimerVehicule(int idVehicule) {
        String query = "DELETE FROM vehicule WHERE id_vehicule = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer tous les véhicules
    public List<Vehicule> getTousVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicule";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vehicule vehicule = new Vehicule(
                        rs.getInt("id_vehicule"),
                        rs.getString("modele"),
                        rs.getString("marque"),
                        rs.getInt("annee"),
                        rs.getString("carburant"),
                        rs.getDouble("prix_location_jour"),
                        rs.getString("etat"),
                        rs.getString("type")
                );
                vehicules.add(vehicule);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }

    // Récupérer un véhicule par ID
    public Vehicule getVehiculeParId(int idVehicule) {
        String query = "SELECT * FROM vehicule WHERE id_vehicule = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicule(
                            rs.getInt("id_vehicule"),
                            rs.getString("modele"),
                            rs.getString("marque"),
                            rs.getInt("annee"),
                            rs.getString("carburant"),
                            rs.getDouble("prix_location_jour"),
                            rs.getString("etat"),
                            rs.getString("type")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
