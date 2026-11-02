package dashboar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportingDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/Location_vehicules";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Rapports : Véhicules les plus loués
    public List<String[]> getTopRentedVehicles() {
        List<String[]> vehicles = new ArrayList<>();
        String sql = "SELECT v.id_vehicule, v.marque, v.modele, COUNT(r.id_reservation) AS total_rentals " +
                "FROM Vehicule v " +
                "JOIN Reservation r ON v.id_vehicule = r.id_vehicule " +
                "GROUP BY v.id_vehicule, v.marque, v.modele " +
                "ORDER BY total_rentals DESC LIMIT 5";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idVehicule = rs.getString("id_vehicule");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String totalRentals = rs.getString("total_rentals");

                vehicles.add(new String[]{idVehicule, marque, modele, totalRentals});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Rapports : Taux d'utilisation des véhicules
    public List<String[]> getVehicleUtilizationRates() {
        List<String[]> rates = new ArrayList<>();
        String sql = "SELECT v.id_vehicule, v.marque, v.modele, " +
                "ROUND((SUM(DATEDIFF(r.date_fin, r.date_debut)) / 365) * 100, 2) AS utilization_rate " +
                "FROM Vehicule v " +
                "LEFT JOIN Reservation r ON v.id_vehicule = r.id_vehicule " +
                "GROUP BY v.id_vehicule, v.marque, v.modele";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idVehicule = rs.getString("id_vehicule");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String utilizationRate = rs.getString("utilization_rate");

                rates.add(new String[]{idVehicule, marque, modele, utilizationRate + "%"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    // Historique des locations : Par client ou véhicule
    public List<String[]> getRentalHistory(String filterType, int filterId) {
        List<String[]> history = new ArrayList<>();
        String sql;

        if ("client".equals(filterType)) {
            sql = "SELECT r.id_reservation, c.nom, c.prenom, r.date_debut, r.date_fin, r.montant_total " +
                    "FROM Reservation r " +
                    "JOIN Client c ON r.id_client = c.id_client " +
                    "WHERE c.id_client = ?";
        } else {
            sql = "SELECT r.id_reservation, v.marque, v.modele, r.date_debut, r.date_fin, r.montant_total " +
                    "FROM Reservation r " +
                    "JOIN Vehicule v ON r.id_vehicule = v.id_vehicule " +
                    "WHERE v.id_vehicule = ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String idReservation = rs.getString("id_reservation");
                    String detail1 = rs.getString(2);
                    String detail2 = rs.getString(3);
                    String dateDebut = rs.getString("date_debut");
                    String dateFin = rs.getString("date_fin");
                    String montantTotal = rs.getString("montant_total");

                    history.add(new String[]{idReservation, detail1, detail2, dateDebut, dateFin, montantTotal});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }
}
