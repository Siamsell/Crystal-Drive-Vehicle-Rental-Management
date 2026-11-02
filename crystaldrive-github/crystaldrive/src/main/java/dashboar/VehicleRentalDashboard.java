package dashboar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.TableRowSorter;

public class VehicleRentalDashboard extends JFrame {

    private JTable vehiclesTable, clientsTable, reservationsTable;
    private JTextField searchFieldVehicles, searchFieldClients, searchFieldReservations;
    private JTextArea alertsArea; // For displaying alerts

    public VehicleRentalDashboard() {
        setTitle("Dashboard - Système de Location de Véhicules");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // Panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Création des onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet pour la gestion des véhicules
        JPanel vehiclesPanel = createVehiclesPanel();
        tabbedPane.addTab("Gestion des Véhicules", vehiclesPanel);

        // Onglet pour la gestion des clients
        JPanel clientsPanel = createClientsPanel();
        tabbedPane.addTab("Gestion des Clients", clientsPanel);

        // Onglet pour la gestion des réservations
        JPanel reservationsPanel = createReservationsPanel();
        tabbedPane.addTab("Gestion des Réservations", reservationsPanel);

        // Onglet pour les alertes
        JPanel alertsPanel = createAlertsPanel();
        tabbedPane.addTab("Alertes et Notifications", alertsPanel);

        // Ajouter les onglets au panneau principal
        panel.add(tabbedPane, BorderLayout.CENTER);

        // Ajouter le panneau principal à la fenêtre
        add(panel);

        // Start a background task to check for vehicle returns and maintenance reminders
        startAlertCheck();
    }

    // Panel pour la gestion des alertes
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // Initialiser alertsArea avant de l'ajouter au panneau
        alertsArea = new JTextArea(10, 40);
        alertsArea.setEditable(false);
        alertsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        alertsArea.setBackground(Color.LIGHT_GRAY);
        alertsArea.setForeground(Color.BLACK);
        alertsArea.setLineWrap(true);
        alertsArea.setWrapStyleWord(true);
        alertsArea.setBorder(BorderFactory.createTitledBorder("Alertes et Notifications"));
        alertsArea.setMargin(new Insets(10, 10, 10, 10)); // Ajoute du padding autour du texte

        JScrollPane scrollPane = new JScrollPane(alertsArea); // Ajouter alertsArea dans une scrollPane
        panel.add(scrollPane, BorderLayout.CENTER);
        // Ajouter la scrollPane ici
        return panel;
    }


    // Panel pour la gestion des véhicules
    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher un véhicule:"));
        searchFieldVehicles = new JTextField(20);
        searchPanel.add(searchFieldVehicles);
        JButton searchButtonVehicles = new JButton("Rechercher");
        searchPanel.add(searchButtonVehicles);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Table pour les véhicules
        String[] columns = {"ID", "Marque", "Modèle", "Année", "Type", "Carburant", "Prix/Jour", "Etat"};
        DefaultTableModel vehiclesModel = new DefaultTableModel(columns, 0);
        vehiclesTable = new JTable(vehiclesModel);
        JScrollPane scrollPaneVehicles = new JScrollPane(vehiclesTable);
        panel.add(scrollPaneVehicles, BorderLayout.CENTER);

        // Remplir la table avec les données des véhicules depuis la base de données
        fillVehiclesTable(vehiclesModel);

        // Action de recherche
        searchButtonVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchFieldVehicles.getText().toLowerCase();
                // Filtrage des données dans la table en fonction de la recherche
                filterTable(vehiclesTable, searchQuery);
            }
        });

        return panel;
    }

    // Panel pour la gestion des clients
    private JPanel createClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher un client:"));
        searchFieldClients = new JTextField(20);
        searchPanel.add(searchFieldClients);
        JButton searchButtonClients = new JButton("Rechercher");
        searchPanel.add(searchButtonClients);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Table pour les clients
        String[] columns = {"ID", "Nom", "Prenom", "Telephone", "Email", "Numero Permis"};
        DefaultTableModel clientsModel = new DefaultTableModel(columns, 0);
        clientsTable = new JTable(clientsModel);
        JScrollPane scrollPaneClients = new JScrollPane(clientsTable);
        panel.add(scrollPaneClients, BorderLayout.CENTER);

        // Remplir la table avec les données des clients depuis la base de données
        fillClientsTable(clientsModel);

        // Action de recherche
        searchButtonClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchFieldClients.getText().toLowerCase();
                // Filtrage des données dans la table en fonction de la recherche
                filterTable(clientsTable, searchQuery);
            }
        });

        return panel;
    }

    // Panel pour la gestion des réservations
    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher une réservation:"));
        searchFieldReservations = new JTextField(20);
        searchPanel.add(searchFieldReservations);
        JButton searchButtonReservations = new JButton("Rechercher");
        searchPanel.add(searchButtonReservations);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Table pour les réservations
        String[] columns = {"ID Réservation", "id-Véhicule", "id-Client", "Date de Réservation", "Date de Retour", "Montant Total", "Statut"};
        DefaultTableModel reservationsModel = new DefaultTableModel(columns, 0);
        reservationsTable = new JTable(reservationsModel);
        JScrollPane scrollPaneReservations = new JScrollPane(reservationsTable);
        panel.add(scrollPaneReservations, BorderLayout.CENTER);

        // Remplir la table avec les données des réservations depuis la base de données
        fillReservationsTable(reservationsModel);

        // Action de recherche
        searchButtonReservations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchFieldReservations.getText().toLowerCase();
                // Filtrage des données dans la table en fonction de la recherche
                filterTable(reservationsTable, searchQuery);
            }
        });

        return panel;
    }

    // Méthode pour vérifier les retards et la maintenance des véhicules
    private void startAlertCheck() {
        Timer timer = new Timer(8000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkVehicleReturnsAndMaintenance();
            }
        });
        timer.start();
    }

    private void checkVehicleReturnsAndMaintenance() {
        String query = "SELECT  r.id_retour, r.id_reservation, r.date_retour, r.etat_retour, res.id_vehicule, res.statut " +
                "FROM retour r " +
                "INNER JOIN reservation res ON r.id_reservation = res.id_reservation " +
                "WHERE res.statut != 'terminé' AND res.statut != 'annulé' AND r.date_retour < NOW();";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules?useSSL=false&serverTimezone=UTC", "root", "");
             Statement statement = connection.createStatement();

             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Date returnDate = resultSet.getDate("date_retour");
                String returnCondition = resultSet.getString("etat_retour");
                int vehicleId = resultSet.getInt("id_vehicule");
                String reservationStatus = resultSet.getString("statut");

                if (returnDate.before(new Date(System.currentTimeMillis())) && "en cours".equalsIgnoreCase(reservationStatus)) {
                    String alertMessage = "Rappel: Le véhicule avec ID " + vehicleId +
                            " n'a pas été retourné à temps. État du retour: " + returnCondition +
                            ". Veuillez prendre les mesures nécessaires.";
                    System.out.println(alertMessage); // Vérifiez si ce message s'affiche
                    alertsArea.append(alertMessage + "\n");// Verifiez si l'alerte est bien ajoutée ici
                }

// Alertes pour les véhicules endommagés
                if ("dommage".equalsIgnoreCase(returnCondition)) {
                    String maintenanceAlert = "Alerte: Le véhicule avec ID " + vehicleId +
                            " nécessite une maintenance en raison d'un état endommagé.";
                    System.out.println(maintenanceAlert); // Vérifiez si ce message s'affiche
                    alertsArea.append(maintenanceAlert + "\n");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace(); // Affiche les erreurs SQL dans la console
        }
    }


    // Méthode pour remplir la table des véhicules avec les données de la base de données
    private void fillVehiclesTable(DefaultTableModel model) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicule")) {

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("id_vehicule"),
                        resultSet.getString("marque"),
                        resultSet.getString("modele"),
                        resultSet.getInt("annee"),
                        resultSet.getString("type"),
                        resultSet.getString("carburant"),
                        resultSet.getString("prix_location_jour"),
                        resultSet.getString("etat")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour remplir la table des clients avec les données de la base de données
    private void fillClientsTable(DefaultTableModel model) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules?useSSL=false&serverTimezone=UTC", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM client")) {

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("id_client"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getString("numero_permis")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour remplir la table des réservations avec les données de la base de données
    private void fillReservationsTable(DefaultTableModel model) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation")) {

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("id_reservation"),
                        resultSet.getInt("id_vehicule"),
                        resultSet.getInt("id_client"),
                        resultSet.getDate("date_debut"),
                        resultSet.getDate("date_fin"),
                        resultSet.getDouble("montant_total"),
                        resultSet.getString("statut")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour filtrer les données dans une table en fonction de la recherche
    private void filterTable(JTable table, String searchQuery) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VehicleRentalDashboard dashboard = new VehicleRentalDashboard();
            dashboard.setVisible(true);
        });
    }
}
