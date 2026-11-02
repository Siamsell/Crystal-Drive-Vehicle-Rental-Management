package dashboar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;


public class InterfaceReservation extends JFrame {
    private JTable table; // Table des réservations
    private DefaultTableModel tableModel; // Modèle pour la table

    private double totalCost = 0.0;



    private JTabbedPane tabbedPane;  // Déclaration de tabbedPane ici
    private JComboBox<String> vehicleComboBox;
    private List<Reservation> reservations = new ArrayList<>();
    private JTextField clientNameField;
    private DefaultTableModel reservationTableModel;

    public InterfaceReservation() {


        clientNameField = new JTextField(20);
        this.add(clientNameField); // Ajout du champ au conteneur (comme JPanel)

        setTitle("Gestion des Réservations et Paiements");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);


        // Charger l'image de fond
        ImageIcon backgroundIcon = new ImageIcon("/images/image.jpg");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        backgroundIcon = new ImageIcon(backgroundImage);


        // Créer un JLabel pour afficher l'image
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());


        // Panneau principal pour les composants
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);

        // Couleurs et polices
        Color darkBlue = new Color(255, 255, 255);
        Color lightBlue = new Color(1, 15, 30);
        Color beige = new Color(255, 245, 230);
        Font titleFont = new Font("SansSerif", Font.BOLD, 18);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);


        // Initialiser le JTabbedPane
        tabbedPane = new JTabbedPane(SwingConstants.LEFT);
        tabbedPane.setBackground(darkBlue);
        tabbedPane.setForeground(beige);
        tabbedPane.setOpaque(false);


        // Panneau "Création de réservation"
        JPanel creationPanel = new JPanel();
        creationPanel.setLayout(new BoxLayout(creationPanel, BoxLayout.Y_AXIS));
        creationPanel.setOpaque(false);
        creationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Créer une nouvelle réservation");
        titleLabel.setFont(titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(darkBlue);

        JLabel vehicleLabel = new JLabel("Sélectionnez un véhicule :");
        vehicleLabel.setFont(labelFont);

// ComboBox vide au départ
        vehicleComboBox = new JComboBox<>();
        vehicleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

// Charger les véhicules depuis la base de données
        List<String> vehicles = loadVehicleModels(); // Méthode précédente
        for (String vehicle : vehicles) {
            vehicleComboBox.addItem(vehicle); // Ajouter chaque véhicule formaté
        }

        JLabel startDateLabel = new JLabel("Date de début :");
        startDateLabel.setFont(labelFont);
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());

        JLabel endDateLabel = new JLabel("Date de fin :");
        endDateLabel.setFont(labelFont);
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());

        JLabel costLabel = new JLabel("Coût estimé :");
        costLabel.setFont(labelFont);
        JTextField costField = new JTextField();
        costField.setEditable(false);

        JButton calculateCostButton = new JButton("Calculer le coût");
        JButton saveButton = new JButton("Valider la réservation");

        calculateCostButton.addActionListener(e -> {
            try {
                // Obtenir les dates
                Date startDate = (Date) startDateSpinner.getValue();
                Date endDate = (Date) endDateSpinner.getValue();

                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(this, "La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Calculer la différence en jours
                long diffInMillis = endDate.getTime() - startDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;

                // Récupérer le modèle sélectionné
                String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
                if (selectedVehicle != null) {
                    // Extraire uniquement le modèle depuis la chaîne "Modèle | Prix DA/jour | État"
                    String[] parts = selectedVehicle.split(" \\| ");
                    String modele = parts[0]; // Récupère le modèle uniquement

                    // Récupérer le prix depuis la base de données
                    double costPerDay = getPriceFromDatabase(modele);

                    // Calculer le coût total
                    double totalCost = diffInDays * costPerDay;
                    costField.setText(String.format("%.2f DA", totalCost));
                } else {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors du calcul du coût.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveButton.addActionListener(e -> {
            try {
                // Récupérer les dates
                Date startDate = (Date) startDateSpinner.getValue();
                Date endDate = (Date) endDateSpinner.getValue();

                // Vérification des dates
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(this, "La date de début doit être avant la date de fin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Vérification du véhicule
                String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
                if (selectedVehicle == null || selectedVehicle.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule.", "Erreur", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Calcul du coût
                long diffInMillis = endDate.getTime() - startDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) + 1;
                double costPerDay = getPriceFromDatabase(selectedVehicle.split(" \\| ")[0]);
                double totalCost = diffInDays * costPerDay;

                // Appel à la base pour sauvegarder
                addreservation(selectedVehicle.split(" \\| ")[0], startDate, endDate, totalCost, "en cours");

                // Mise à jour de l'affichage des réservations


            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur de validation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

// Méthode pour mettre à jour le tableau de suivi


// Méthode pour afficher la page de suivi

        // Agencement vertical
        creationPanel.add(titleLabel);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        creationPanel.add(vehicleLabel);
        creationPanel.add(vehicleComboBox);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creationPanel.add(startDateLabel);
        creationPanel.add(startDateSpinner);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creationPanel.add(endDateLabel);
        creationPanel.add(endDateSpinner);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creationPanel.add(costLabel);
        creationPanel.add(costField);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        creationPanel.add(calculateCostButton);
        creationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        creationPanel.add(saveButton);

        tabbedPane.add("Créer une réservation", creationPanel);


        // Panneau "Paiements"
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setBackground(lightBlue);

        String[] paymentColumns = {"Mode de paiement", "Montant", "Date"};
        DefaultTableModel paymentTableModel = new DefaultTableModel(new Object[][]{}, paymentColumns);
        JTable paymentTable = new JTable(paymentTableModel);
        paymentTable.setBackground(beige);
        paymentTable.setForeground(darkBlue);

        JScrollPane tableScrollPane = new JScrollPane(paymentTable);

        JPanel addPaymentPanel = new JPanel();
        addPaymentPanel.setLayout(new BoxLayout(addPaymentPanel, BoxLayout.Y_AXIS));
        addPaymentPanel.setBackground(lightBlue);
        addPaymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel paymentLabel = new JLabel("Ajouter un paiement");
        paymentLabel.setFont(titleFont);
        paymentLabel.setForeground(darkBlue);

        JLabel modeLabel = new JLabel("Mode de paiement :");
        modeLabel.setFont(labelFont);
        JComboBox<String> paymentModeCombo = new JComboBox<>(new String[]{"Caisse", "Banque", "CCP"});

        JLabel amountLabel = new JLabel("Montant :");
        amountLabel.setFont(labelFont);
        JTextField amountField = new JTextField();

        JButton addPaymentButton = new JButton("Enregistrer le paiement");

        JButton printReceiptButton = new JButton("Imprimer le reçu");
        addPaymentButton.addActionListener(e -> {
            String modePaiement = (String) paymentModeCombo.getSelectedItem();
            String montantText = amountField.getText().trim();

            if (modePaiement == null || modePaiement.isEmpty() || montantText.isEmpty()) {
                JOptionPane.showMessageDialog(paymentPanel, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double montant = Double.parseDouble(montantText);
                int idReservation = 1; // À remplacer par une logique réelle pour sélectionner la réservation
                enregistrerPaiement(idReservation, montant, modePaiement);
                // Ajouter au tableau pour affichage immédiat
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                paymentTableModel.addRow(new Object[]{modePaiement, montant, date});
                amountField.setText(""); // Réinitialiser le champ montant
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(paymentPanel, "Veuillez entrer un montant valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

// Action pour imprimer le reçu
        printReceiptButton.addActionListener(e -> {
            int selectedRow = paymentTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(paymentPanel, "Veuillez sélectionner un paiement à imprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Récupérer les détails du paiement sélectionné
            String mode = (String) paymentTableModel.getValueAt(selectedRow, 0);
            String amount = (String) paymentTableModel.getValueAt(selectedRow, 1);
            String date = (String) paymentTableModel.getValueAt(selectedRow, 2);

            // Créer une fenêtre de dialogue
            JDialog printDialog = new JDialog((Frame) null, "Détails du reçu", true);
            printDialog.setSize(400, 200);
            printDialog.setLayout(new BorderLayout());

            // Créer un panneau pour les informations
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            infoPanel.add(new JLabel("Mode de paiement : " + mode));
            infoPanel.add(new JLabel("Montant : " + amount + " DA"));
            infoPanel.add(new JLabel("Date : " + date));

            // Ajouter des boutons "Imprimer" et "Annuler"
            JPanel diabuttonPanel = new JPanel();
            JButton confirmButton = new JButton("Imprimer");
            JButton diacancelButton = new JButton("Annuler");

            confirmButton.addActionListener(printEvent -> {
                JOptionPane.showMessageDialog(paymentPanel, "Le reçu a été imprimé avec succès.", "Impression", JOptionPane.INFORMATION_MESSAGE);
                printDialog.dispose(); // Fermer la fenêtre après l'impression
            });

            diacancelButton.addActionListener(cancelEvent -> printDialog.dispose());

            diabuttonPanel.add(confirmButton);
            diabuttonPanel.add(diacancelButton);

            // Ajouter les panneaux au dialogue
            printDialog.add(infoPanel, BorderLayout.CENTER);
            printDialog.add(diabuttonPanel, BorderLayout.SOUTH);

            // Afficher le dialogue
            printDialog.setLocationRelativeTo(paymentPanel);
            printDialog.setVisible(true);
        });

// Ajouter les composants au panneau
        addPaymentPanel.add(paymentLabel);
        addPaymentPanel.add(Box.createVerticalStrut(10));
        addPaymentPanel.add(modeLabel);
        addPaymentPanel.add(paymentModeCombo);
        addPaymentPanel.add(Box.createVerticalStrut(10));
        addPaymentPanel.add(amountLabel);
        addPaymentPanel.add(amountField);
        addPaymentPanel.add(Box.createVerticalStrut(20));
        addPaymentPanel.add(addPaymentButton);
        addPaymentPanel.add(Box.createVerticalStrut(10));
        addPaymentPanel.add(printReceiptButton);

        paymentPanel.add(tableScrollPane, BorderLayout.CENTER);
        paymentPanel.add(addPaymentPanel, BorderLayout.EAST);


        tabbedPane.add("Gestion des paiements", paymentPanel);


        Color ddarkBlue = new Color(33, 73, 91);
        Color bbeige = new Color(245, 245, 220);
        Color llightBlue = new Color(33, 73, 91);

        // Appelle la méthode qui crée et retourne le panel



        // Ajouter le JTabbedPane à la fenêtre principale
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        backgroundLabel.add(mainPanel);
        add(backgroundLabel);

        setVisible(true);
    }



    private List<String> loadVehicleModels() {
        List<String> vehicles = new ArrayList<>();
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
            String query = "SELECT modele, prix_location_jour, etat FROM vehicule";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Récupérer les données et les ajouter dans la liste
            while (rs.next()) {
                String modele = rs.getString("modele");
                double prix = rs.getDouble("prix_location_jour");
                String etat = rs.getString("etat");

                // Formater l'affichage : "Modèle | Prix €/jour | État"
                String formattedVehicle = String.format("%s | %.2f DA/jour | %s", modele, prix, etat);
                vehicles.add(formattedVehicle);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    private double getPriceFromDatabase(String modele) {
        double price = 0.0;
        try {
            // Connexion à la base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");

            // Préparer une requête pour récupérer le prix par jour du véhicule
            String query = "SELECT prix_location_jour FROM vehicule WHERE modele = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, modele);

            // Exécuter la requête
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("prix_location_jour");
            }

            // Fermer la connexion
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération du prix.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return price;
    }


    private void updateReservations(JTable reservationsTable, DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount(); // Nombre total de lignes

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "")) {
            // Préparation de la requête SQL pour mettre à jour les réservations
            String updateQuery = "UPDATE reservation SET date_debut = ?, date_fin = ?, montant_total = ?, statut = ? WHERE id_reservation = ?";
            PreparedStatement ps = conn.prepareStatement(updateQuery);

            for (int i = 0; i < rowCount; i++) {
                try {
                    // Récupération des données à partir du modèle de table
                    int id_reservation = (int) tableModel.getValueAt(i, 0); // ID Réservation
                    String date_Debut = (String) tableModel.getValueAt(i, 3); // Date Début
                    String date_Fin = (String) tableModel.getValueAt(i, 4); // Date Fin
                    double montant_Total = (double) tableModel.getValueAt(i, 5); // Montant Total
                    String statut = (String) tableModel.getValueAt(i, 6); // Statut

                    // Affectation des paramètres pour la requête
                    ps.setString(1, date_Debut);
                    ps.setString(2, date_Fin);
                    ps.setDouble(3, montant_Total);
                    ps.setString(4, statut);
                    ps.setInt(5, id_reservation);

                    ps.addBatch(); // Ajoute la requête au batch
                } catch (Exception ex) {
                    // Affiche une erreur si une ligne ne peut pas être traitée
                    System.err.println("Erreur lors du traitement de la ligne " + i + " : " + ex.getMessage());
                }
            }

            // Exécuter les requêtes en batch
            int[] updateCounts = ps.executeBatch();
            JOptionPane.showMessageDialog(null, "Les modifications ont été enregistrées avec succès. Total : " + updateCounts.length);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    // Méthode pour annuler une réservation
    private void annulerReservation() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int idReservation = (int) table.getValueAt(row, 0);
            try {
                // Connexion à la base de données
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
                String query = "UPDATE reservation SET statut = 'Annulée' WHERE id_reservation = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, idReservation);
                ps.executeUpdate();

                table.setValueAt("Annulée", row, 6); // Mettre à jour directement dans le tableau
                JOptionPane.showMessageDialog(this, "Réservation annulée !");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur d'annulation : " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation !");
        }
    }


    private List<String[]> getAllReservationsFromDatabase() {
        List<String[]> reservations = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules ", "root", "");

            String query = "SELECT r.id_reservation, v.marque, v.modele, r.date_debut, r.date_fin, r.statut " +
                    "FROM reservation r " +
                    "JOIN vehicule v ON r.id_vehicule = v.id_vehicule";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] row = {
                        rs.getString("id_reservation"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getString("date_debut"),
                        rs.getString("date_fin"),
                        rs.getString("etat_reservation")
                };
                reservations.add(row);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }


    private void updateReservation(int idReservation, String newEtat, Date newStartDate, Date newEndDate) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nom_base", "user", "password");

            String query = "UPDATE reservation SET date_debut = ?, date_fin = ?, etat_reservation = ? WHERE id_reservation = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDate(1, new java.sql.Date(newStartDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(newEndDate.getTime()));
            pstmt.setString(3, newEtat);
            pstmt.setInt(4, idReservation);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Réservation mise à jour avec succès !");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour vérifier la connexion à la base de données
    private boolean checkDatabaseConnection() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }








    Color darkBlue = new Color(4, 39, 54);
    Color beige = new Color(245, 245, 220);
    Color lightBlue = new Color(33, 73, 91);


    private void enregistrerPaiement(int idReservation, double montant, String modePaiement) {
        String query = "INSERT INTO paiement (id_reservation, montant, date_paiement, mode_paiement) VALUES (?, ?, CURDATE(), ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/location_vehicules", "root", "");
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idReservation); // ID de la réservation
            ps.setDouble(2, montant);   // Montant du paiement
            ps.setString(3, modePaiement); // Mode de paiement

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {


            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Paiement enregistré avec succès ! " );
        }
    }




    // Méthode pour afficher une erreur
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }







    // Conversion des dates de format String à Date
    public Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur de format de date : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void addreservation(String vehicleModel, Date startDate, Date endDate, double totalCost, String status) {
        String url = "jdbc:mysql://localhost:3306/location_vehicules"; // Change avec ton URL
        String user = "root"; // Remplace par ton utilisateur
        String password = ""; // Mot de passe de ta base

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO reservation (id_vehicule,id_client, date_debut, date_fin,montant_total , statut)\n" +
                    "SELECT id_vehicule,1, ?, ?, ?, ?\n" +
                    "FROM vehicule\n" +
                    "WHERE modele = ?;\n";
            PreparedStatement pstmt = conn.prepareStatement(query);


            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            pstmt.setDouble(3, totalCost);
            pstmt.setString(4, status);
            pstmt.setString(5, vehicleModel);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Réservation sauvegardée dans la base.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'insertion dans la base : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Méthode pour mettre à jour le tableau de suivi
    private void updateSuiviReservationTable(DefaultTableModel tableModel) {
        // Vider le tableau actuel
        tableModel.setRowCount(0);

        // Ajouter chaque réservation au tableau
        for (Reservation reservation : reservations) {
            Object[] row = {
                    reservation.getVehicle(),
                    reservation.getClientName(),
                    reservation.getStartDate() + " - " + reservation.getEndDate(),
                    reservation.getStatus()
            };
            tableModel.addRow(row);
        }
    }






    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceReservation());
    }
}