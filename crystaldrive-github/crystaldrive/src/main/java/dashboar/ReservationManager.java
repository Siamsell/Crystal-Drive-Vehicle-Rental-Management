package dashboar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    private JTable reservationsTable;

    public ReservationManager() {
        displayReservations();
    }

    public void displayReservations() {
        // Définir les colonnes du tableau
        String[] columnNames = {"ID Réservation", "Client", "Véhicule", "Date Début", "Date Fin", "Montant Total", "Statut"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Colonnes modifiables
                return column == 3 || column == 4 || column == 5 || column == 6;
            }
        };

        reservationsTable = new JTable(tableModel);

        // Ajouter un JComboBox pour la colonne "Statut"
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"En cours", "Terminée", "Annulée"});
        reservationsTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(statusComboBox));

        // Charger les données depuis la base de données
        loadReservations(tableModel);

        // Ajouter un JScrollPane pour le tableau
        JScrollPane scrollPane = new JScrollPane(reservationsTable);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Enregistrer les modifications");
        JButton cancelButton = new JButton("Annuler Réservation");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Ajouter des actions aux boutons
        saveButton.addActionListener(e -> updateReservations(tableModel));
        cancelButton.addActionListener(e -> cancelReservation());

        // Configurer la fenêtre
        JFrame frame = new JFrame("Suivi des Réservations");
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void loadReservations(DefaultTableModel tableModel) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "")) {
            String query = "SELECT r.id_reservation AS ID, " +
                    "CONCAT(c.nom, ' ', c.prenom) AS Client, " +
                    "CONCAT(v.marque, ' ', v.modele) AS Véhicule, " +
                    "r.date_debut AS 'Date Début', r.date_fin AS 'Date Fin', " +
                    "r.montant_total AS 'Montant Total', r.statut AS Statut " +
                    "FROM reservation r " +
                    "JOIN vehicule v ON r.id_vehicule = v.id_vehicule " +
                    "JOIN client c ON r.id_client = c.id_client";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("Client"),
                        rs.getString("Véhicule"),
                        rs.getString("Date Début"),
                        rs.getString("Date Fin"),
                        rs.getDouble("Montant Total"),
                        rs.getString("Statut")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des réservations : " + e.getMessage());
        }
    }

    private void updateReservations(DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "")) {
            String query = "UPDATE reservation SET date_debut = ?, date_fin = ?, montant_total = ?, statut = ? WHERE id_reservation = ?";
            PreparedStatement ps = conn.prepareStatement(query);

            for (int i = 0; i < rowCount; i++) {
                try {
                    int id = (int) tableModel.getValueAt(i, 0);
                    String dateDebut = (String) tableModel.getValueAt(i, 3);
                    String dateFin = (String) tableModel.getValueAt(i, 4);
                    double montant = (double) tableModel.getValueAt(i, 5);
                    String statut = (String) tableModel.getValueAt(i, 6);

                    ps.setString(1, dateDebut);
                    ps.setString(2, dateFin);
                    ps.setDouble(3, montant);
                    ps.setString(4, statut);
                    ps.setInt(5, id);

                    ps.addBatch();
                } catch (Exception ex) {
                    System.err.println("Erreur à la ligne " + i + ": " + ex.getMessage());
                }
            }

            int[] updateCounts = ps.executeBatch();
            JOptionPane.showMessageDialog(null, "Modifications enregistrées : " + updateCounts.length + " lignes mises à jour.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    private void cancelReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow != -1) {
            int idReservation = (int) reservationsTable.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "")) {
                String query = "UPDATE reservation SET statut = 'Annulée' WHERE id_reservation = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, idReservation);
                ps.executeUpdate();

                reservationsTable.setValueAt("Annulée", selectedRow, 6);
                JOptionPane.showMessageDialog(null, "Réservation annulée avec succès !");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'annulation : " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation à annuler.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationManager().displayReservations());
    }
}
