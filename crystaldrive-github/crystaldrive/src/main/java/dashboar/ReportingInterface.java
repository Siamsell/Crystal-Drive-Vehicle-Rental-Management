package dashboar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportingInterface {

    public ReportingInterface() {
        // Création de la fenêtre principale
        JFrame frame = new JFrame("Rapports et Historique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(105, 174, 211));

        // Bannière
        JLabel bannerLabel = new JLabel("Rapports de Location", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        bannerLabel.setForeground(Color.WHITE);
        frame.add(bannerLabel, BorderLayout.NORTH);

        // Création du JTabbedPane pour basculer entre les tables
        JTabbedPane tabbedPane = new JTabbedPane();

        // Table pour les véhicules
        String[] vehicleColumns = {"ID", "Marque", "Modèle", "Date Début", "Date Fin", "Montant Total", "Taux/Total"};
        DefaultTableModel vehicleTableModel = new DefaultTableModel(vehicleColumns, 0);
        JTable vehicleTable = new JTable(vehicleTableModel);
        customizeTable(vehicleTable);

        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);
        vehicleScrollPane.getViewport().setBackground(new Color(95, 137, 172));
        TitledBorder vehicleBorder = BorderFactory.createTitledBorder("Locations des Véhicules");
        customizeBorder(vehicleBorder);
        vehicleScrollPane.setBorder(vehicleBorder);

        tabbedPane.addTab("Véhicules", vehicleScrollPane);

        // Table pour les clients
        String[] clientColumns = {"ID Client", "Nom", "Prénom", "Date Début", "Date Fin", "Montant Total", "Statut"};
        DefaultTableModel clientTableModel = new DefaultTableModel(clientColumns, 0);
        JTable clientTable = new JTable(clientTableModel);
        customizeTable(clientTable);

        JScrollPane clientScrollPane = new JScrollPane(clientTable);
        clientScrollPane.getViewport().setBackground(new Color(95, 137, 172));
        TitledBorder clientBorder = BorderFactory.createTitledBorder("Locations des Clients");
        customizeBorder(clientBorder);
        clientScrollPane.setBorder(clientBorder);

        tabbedPane.addTab("Clients", clientScrollPane);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setPreferredSize(new Dimension(900, 150));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton topRentedButton = createButton("Véhicules les plus loués");
        JButton utilizationRateButton = createButton("Taux d'utilisation");
        JButton rentalHistoryButton = createButton("Historique des locations");

        // Action pour afficher les véhicules les plus loués
        topRentedButton.addActionListener(e -> {
            ReportingDAO dao = new ReportingDAO();
            List<String[]> vehicles = dao.getTopRentedVehicles();
            vehicleTableModel.setRowCount(0); // Vider la table des véhicules
            for (String[] row : vehicles) {
                vehicleTableModel.addRow(new Object[]{row[0], row[1], row[2], "-", "-", "-", "Total : " + row[3]});
            }
        });

        // Action pour afficher les taux d'utilisation des véhicules
        utilizationRateButton.addActionListener(e -> {
            ReportingDAO dao = new ReportingDAO();
            List<String[]> rates = dao.getVehicleUtilizationRates();
            vehicleTableModel.setRowCount(0); // Vider la table des véhicules
            for (String[] row : rates) {
                vehicleTableModel.addRow(new Object[]{row[0], row[1], row[2], "-", "-", "-", "Taux : " + row[3]});
            }
        });

        // Action pour afficher l'historique des locations
        rentalHistoryButton.addActionListener(e -> {
            try {
                String currentTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                String filterType = currentTab.equalsIgnoreCase("Véhicules") ? "vehicule" : "client";

                int filterId = Integer.parseInt(JOptionPane.showInputDialog("Entrer l'ID :"));
                ReportingDAO dao = new ReportingDAO();
                if ("vehicule".equalsIgnoreCase(filterType)) {
                    List<String[]> history = dao.getRentalHistory(filterType, filterId);
                    vehicleTableModel.setRowCount(0);
                    for (String[] row : history) {
                        vehicleTableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], "-"});
                    }
                } else if ("client".equalsIgnoreCase(filterType)) {
                    List<String[]> history = dao.getRentalHistory(filterType, filterId);
                    clientTableModel.setRowCount(0);
                    for (String[] row : history) {
                        clientTableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5], "Actif"});
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID invalide ou aucune entrée fournie.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Une erreur s'est produite : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ajouter les boutons au panel
        buttonPanel.add(topRentedButton);
        buttonPanel.add(utilizationRateButton);
        buttonPanel.add(rentalHistoryButton);

        // Ajouter les composants au frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Méthode pour créer des boutons stylisés
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(250, 60));
        button.setFocusPainted(false);
        button.setBackground(new Color(95, 137, 172));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    // Méthode pour styliser les tables
    private static void customizeTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setBackground(new Color(166, 187, 205));
        table.setOpaque(true);
    }

    // Méthode pour styliser les bordures des panneaux
    private static void customizeBorder(TitledBorder border) {
        border.setTitleFont(new Font("Arial", Font.BOLD, 20));
        border.setTitleJustification(TitledBorder.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReportingInterface::new);
    }
}
