package dashboar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class GestionnairePage extends JFrame {
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public GestionnairePage() {
        // Configuration de la fenêtre principale
        setTitle("Gestionnaire - Page Globale");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245)); // Arrière-plan clair

        // Connexion à la base de données
        connectToDatabase();

        // Titre
        JLabel titleLabel = new JLabel("Gestionnaire - Interface Principale", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(54, 54, 54));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Section: Table des clients
        tableModel = new DefaultTableModel(new String[]{"ID Client", "Nom", "Prénom", "Téléphone", "Email", "N° Permis"}, 0);
        clientTable = new JTable(tableModel);
        clientTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clientTable.setRowHeight(25);
        clientTable.setSelectionBackground(new Color(200, 230, 255));
        clientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadClientData(); // Charger les données depuis la base
        JScrollPane scrollPane = new JScrollPane(clientTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des Clients"));
        add(scrollPane, BorderLayout.CENTER);

        // Section: Boutons pour gérer les clients
        JPanel clientPanel = new JPanel();
        clientPanel.setBackground(new Color(245, 245, 245));
        JButton addClientButton = createStyledButton("Ajouter Client");
        clientPanel.add(addClientButton);
        add(clientPanel, BorderLayout.EAST);

        // Actions des boutons
        addClientButton.addActionListener(e -> addClient());

        // Section: Menu de navigation
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton vehicleManagementButton = createStyledButton("Gestion des Véhicules");
        JButton reservationButton = createStyledButton("Gestion des Réservations");
        JButton returnButton = createStyledButton("Gestion des Retours");
        returnButton.addActionListener(e -> {// Ferme la fenêtre actuelle
            new MainApp(); // Ouvre la fenêtre GestionVehicule
        });
        JButton reportButton = createStyledButton("Consultation des Rapports");
        JButton notificationButton = createStyledButton("Notifications et Rappels");
        JButton suivrereservation = createStyledButton("suiver les reservations");
        menuPanel.add(vehicleManagementButton);
        menuPanel.add(reservationButton);
        menuPanel.add(returnButton);
        menuPanel.add(reportButton);
        menuPanel.add(notificationButton);
        menuPanel.add(suivrereservation);
        add(menuPanel, BorderLayout.SOUTH);

        // Actions pour les boutons de menu
        vehicleManagementButton.addActionListener(e -> navigateTo("Gestion des Véhicules"));
        reservationButton.addActionListener(e -> navigateTo("Gestion des Réservations"));
        returnButton.addActionListener(e -> navigateTo("Gestion des Retours"));
        reportButton.addActionListener(e -> navigateTo("Consultation des Rapports"));
        notificationButton.addActionListener(e -> navigateTo("Notifications et Rappels"));
        suivrereservation.addActionListener(e -> navigateTo("suiver les reservations"));
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(4, 57, 52));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(34, 34, 34), 1));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Connexion à la base de données
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données : " + e.getMessage());
            System.exit(1);
        }
    }

    // Charger les données des clients dans la table
    private void loadClientData() {
        try {
            tableModel.setRowCount(0); // Vider la table avant de recharger les données
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM client");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_client"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("numero_permis")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données : " + e.getMessage());
        }
    }

    // Ajouter un client
    private void addClient() {
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField permisField = new JTextField();

        Object[] fields = {
                "Nom :", nomField,
                "Prénom :", prenomField,
                "Téléphone :", telephoneField,
                "Email :", emailField,
                "N° Permis :", permisField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Ajouter un Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String query = "INSERT INTO client (nom, prenom, telephone, email, numero_permis) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, nomField.getText());
                pstmt.setString(2, prenomField.getText());
                pstmt.setString(3, telephoneField.getText());
                pstmt.setString(4, emailField.getText());
                pstmt.setString(5, permisField.getText());
                pstmt.executeUpdate();
                loadClientData(); // Recharger la table
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client : " + e.getMessage());
            }
        }
    }

    private void navigateTo(String section) {
        switch (section) {
            case "Gestion des Véhicules":
                new MainAppp();
                break;
            case "Gestion des Réservations":
              new InterfaceReservation();
                break;
            case "Gestion des Retours":
                new MainApp();
                break;
            case "Consultation des Rapports":
                new ReportingInterface();
                break;
            case "Notifications et Rappels":
                new VehicleRentalDashboard();
                break;
            case "suiver les reservations":
                new ReservationManager();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Section inconnue : " + section, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestionnairePage::new);
    }
}
