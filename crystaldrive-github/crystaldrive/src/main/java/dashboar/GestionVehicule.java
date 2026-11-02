package dashboar;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.border.AbstractBorder;
import java.awt.geom.RoundRectangle2D;
import java.sql.PreparedStatement;


public class GestionVehicule extends JFrame {

    // Composants de l'interface
    private JTable vehiculetable;
    private DefaultTableModel tableModel;
    private JTextField tfModele, tfMarque, tfAnnee, tfPrix, tfType;
    private JComboBox<String> cbEtat, cbCarburant;
    private JTable rechercheTable;
    private DefaultTableModel rechercheTableModel;

    // Classe pour la bordure arrondie
    public static class RoundedBorder extends AbstractBorder {
        private final int radius; // Rayon des coins arrondis

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Pour un lissage de la bordure
            g2d.setColor(Color.GRAY); // Couleur de la bordure
            g2d.setStroke(new BasicStroke(2)); // Épaisseur de la bordure

            // Dessiner la bordure arrondie
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5); // Espacement interne autour du texte
        }
    }


    public GestionVehicule(){


        // Paramètre de l'interface
        setTitle("Gestion des Véhicules");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Appliquer la couleur grise aux titres des onglets
        UIManager.put("TabbedPane.selected", Color.GRAY);
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.tabAreaBackground", Color.WHITE);



        // Créer un JTabbedPane pour diviser l'interface en trois onglets
        JTabbedPane tabbedPane = new JTabbedPane();



        // 1. Onglet pour gérer les véhicules (ajouter, modifier, supprimer)
        JPanel gestionPanel = new JPanel();
        gestionPanel.setLayout(new BorderLayout());




        // Panel de formulaire pour ajouter, modifier et supprimer un véhicule
        JPanel formPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Chargez l'image de fond
                Image background = new ImageIcon("src/fat.png").getImage();

                // Dessinez l'image de fond sur toute la surface du composant
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

                // Vous pouvez ajouter d'autres éléments graphiques ici (par exemple, des textes, des boutons, etc.)
            }
        };
        formPanel.setLayout(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),"Informations du Véhicule",TitledBorder.LEFT,TitledBorder.TOP,new Font("Arial",Font.BOLD,14),Color.GRAY));
        formPanel.setBackground(Color.GRAY);

        formPanel.add(new JLabel("Modèle:"));
        tfModele = new JTextField();
        tfModele.setBorder(new RoundedBorder(15));

        formPanel.add(tfModele);

        formPanel.add(new JLabel("Marque:"));
        tfMarque = new JTextField();
        tfMarque.setBorder(new RoundedBorder(15));
        formPanel.add(tfMarque);

        formPanel.add(new JLabel("Année:"));
        tfAnnee = new JTextField();
        tfAnnee.setBorder(new RoundedBorder(15));
        formPanel.add(tfAnnee);

        formPanel.add(new JLabel("Carburant:"));
        cbCarburant = new JComboBox<>(new String[] {"Essence", "Diesel", "Électrique", "Hybride"});
        cbCarburant.setBorder(new RoundedBorder(15));
        formPanel.add(cbCarburant);

        formPanel.add(new JLabel("Prix (par jour):"));
        tfPrix = new JTextField();
        tfPrix.setBorder(new RoundedBorder(15));
        formPanel.add(tfPrix);

        formPanel.add(new JLabel("État:"));
        cbEtat = new JComboBox<>(new String[] {"Disponible", "Loué", "En Maintenance"});
        cbEtat.setBorder(new RoundedBorder(15));
        formPanel.add(cbEtat);

        formPanel.add(new JLabel("Type:"));
        tfType = new JTextField();
        tfType.setBorder(new RoundedBorder(15));
        formPanel.add(tfType);

        // Buttons pour ajouter, modifier et supprimer un véhicule
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.white); // Fond blanc pour le panneau des boutons

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(Color.gray);
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterVehicule();
            }
        });
        buttonPanel.add(btnAjouter);






        // Ajout du formulaire et des boutons dans l'onglet gestion
        gestionPanel.add(formPanel, BorderLayout.CENTER);
        gestionPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 2. Onglet pour afficher la liste des véhicules
        JPanel tablePanel = new JPanel(){
            // Méthode pour dessiner l'image de fond
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Chargez l'image de fond
                Image background = new ImageIcon("src/fat.png").getImage();

                // Dessinez l'image de fond sur toute la surface du composant
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

                // Vous pouvez ajouter d'autres éléments graphiques ici (par exemple, des textes, des boutons, etc.)
            }
        };
        String[] columnNames = {"id_vehicule","Modèle", "Marque", "Année", "Carburant", "Prix/jour", "État", "Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        vehiculetable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehiculetable);
        tablePanel.add(scrollPane);

        // Personnalisation du tableau
        vehiculetable.setBackground(new Color(255, 255, 255));
        vehiculetable.setSelectionBackground(new Color(153, 204, 255));
        vehiculetable.setGridColor(new Color(200, 200, 200));
        vehiculetable.setRowHeight(25);


// Ajout des boutons "Modifier" et "Supprimer" dans le deuxième onglet
        JPanel buttonPanelTable = new JPanel();
        buttonPanelTable.setLayout(new FlowLayout());

// Add the btnModifier button correctly

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setBackground(Color.gray);
        btnModifier.setForeground(Color.WHITE);
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierVehicule();
            }
        });
        buttonPanelTable.add(btnModifier);


        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(Color.gray);
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerVehicule();
            }
        });
        buttonPanelTable.add(btnSupprimer);

        tablePanel.add(buttonPanelTable, BorderLayout.SOUTH);



        // Ajouter les composants au JTabbedPane
        tabbedPane.addTab("Gestion des Véhicules", gestionPanel);
        tabbedPane.addTab("État des Véhicules", tablePanel);


        // 3. Onglet pour rechercher des véhicules

        JPanel recherchePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Chargez l'image de fond
                Image background = new ImageIcon("src/fat.png").getImage();

                // Dessinez l'image de fond sur toute la surface du composant
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

                // Vous pouvez ajouter d'autres éléments graphiques ici (par exemple, des textes, des boutons, etc.)
            }

        };

        recherchePanel.setLayout(new BorderLayout());

        // Panneau de filtrage
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(3, 2, 10, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtres de Recherche"));

        filterPanel.add(new JLabel("Marque:"));
        JTextField tfRechercheMarque = new JTextField();
        tfRechercheMarque.setBorder(new RoundedBorder(15));
        filterPanel.add(tfRechercheMarque);

        filterPanel.add(new JLabel("Carburant:"));
        JComboBox<String> cbRechercheCarburant = new JComboBox<>(new String[] {"Essence", "Diesel", "Électrique", "Hybride"});
        cbRechercheCarburant.setBorder(new RoundedBorder(15)); // Vous l'avez bien ici
        filterPanel.add(cbRechercheCarburant);

        filterPanel.add(new JLabel("État:"));
        JComboBox<String> cbRechercheEtat = new JComboBox<>(new String[] {"Disponible", "Loué", "En Maintenance"});
        cbRechercheEtat.setBorder(new RoundedBorder(15));  // Utilisez cette variable
        filterPanel.add(cbRechercheEtat);

        // Bouton de recherche
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(Color.gray);
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherVehicule(tfRechercheMarque.getText(),
                        (String) cbRechercheCarburant.getSelectedItem(),
                        (String) cbRechercheEtat.getSelectedItem());
            }

        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(btnRechercher);
        recherchePanel.add(filterPanel, BorderLayout.NORTH);
        recherchePanel.add(searchPanel, BorderLayout.SOUTH);

        // Tableau des résultats de recherche
        rechercheTableModel = new DefaultTableModel(columnNames, 0);
        rechercheTable = new JTable(rechercheTableModel);
        JScrollPane rechercheScrollPane = new JScrollPane(rechercheTable);
        recherchePanel.add(rechercheScrollPane, BorderLayout.CENTER);

        // Ajouter le panneau de recherche dans l'onglet recherche
        tabbedPane.addTab("Recherche de Véhicules", recherchePanel);

        // Ajouter le JTabbedPane à la fenêtre
        add(tabbedPane);

        // Ajouter un écouteur de clic pour remplir les champs avec les données du véhicule sélectionné
        // Ajouter un écouteur de clic pour remplir les champs avec les données du véhicule sélectionné
        vehiculetable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = vehiculetable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Récupérer les valeurs des colonnes et les attribuer aux champs
                    tfModele.setText((String) tableModel.getValueAt(selectedRow, 1));  // Modele (String)
                    tfMarque.setText((String) tableModel.getValueAt(selectedRow, 2));  // Marque (String)

                    // L'année est un Integer, vous devez la récupérer et la convertir correctement en String
                    Object anneeObject = tableModel.getValueAt(selectedRow, 3); // Annee (Object)
                    if (anneeObject instanceof Integer) {
                        Integer annee = (Integer) anneeObject;  // Convertir en Integer si c'est bien un Integer
                        tfAnnee.setText(annee.toString());  // Conversion en String
                    } else {
                        tfAnnee.setText("");  // Si la valeur est incorrecte, on vide le champ
                    }

                    // Le prix est un Double, vous devez aussi le convertir correctement
                    Object prixObject = tableModel.getValueAt(selectedRow, 5);  // Prix (Object)
                    if (prixObject instanceof Double) {
                        Double prix = (Double) prixObject;  // Convertir en Double si c'est bien un Double
                        tfPrix.setText(String.format("%.2f", prix));  // Conversion en String avec formatage à 2 décimales
                    } else {
                        tfPrix.setText("");  // Si la valeur est incorrecte, on vide le champ
                    }

                    // Remplir les autres champs
                    cbCarburant.setSelectedItem(tableModel.getValueAt(selectedRow, 4));  // Carburant (String)
                    cbEtat.setSelectedItem(tableModel.getValueAt(selectedRow, 6));  // Etat (String)
                    tfType.setText((String) tableModel.getValueAt(selectedRow, 7));  // Type (String)
                }
            }
        });

    }

    // Ajouter un véhicule
    private void ajouterVehicule() {
        String modele = tfModele.getText();
        String marque = tfMarque.getText();
        String annee = tfAnnee.getText();
        String carburant = (String) cbCarburant.getSelectedItem();
        String prix = tfPrix.getText();
        String etat = (String) cbEtat.getSelectedItem();
        String type = tfType.getText();

        // Vérification si les champs nécessaires sont remplis
        if (modele.isEmpty() || marque.isEmpty() || annee.isEmpty() || prix.isEmpty() || type.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification de la validité des entrées pour l'année et le prix
        int anneeValue = 0;
        double prixValue = 0.0;
        try {
            anneeValue = Integer.parseInt(annee);  // Tentative de conversion de l'année
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'année doit être un nombre valide !");
            return;
        }
        try {
            prixValue = Double.parseDouble(prix);  // Tentative de conversion du prix
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide !");
            return;
        }

        // Connexion à la base de données et insertion des données
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO vehicule (modele, marque, annee,  carburant, prix_location_jour, etat,type) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (var stmt = conn.prepareStatement(query)) {
                stmt.setString(1, modele);
                stmt.setString(2, marque);
                stmt.setInt(3, anneeValue);
                stmt.setString(4, carburant);
                stmt.setDouble(5, prixValue);
                stmt.setString(6, etat);
                stmt.setString(7,type);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Véhicule ajouté avec succès!");
                clearFields();
                loadVehicules();  // Rafraîchir la liste des véhicules
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du véhicule.");
        }
    }

    private void modifierVehicule() {
        int selectedRow = vehiculetable.getSelectedRow(); // Vérifie si une ligne est sélectionnée
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule dans le tableau !");
            return;
        }

        // Récupération des valeurs des champs de saisie
        String modele = tfModele.getText().trim();
        String marque = tfMarque.getText().trim();
        String annee = tfAnnee.getText().trim();
        String carburant = (String) cbCarburant.getSelectedItem();
        String prix = tfPrix.getText().trim();
        String etat = (String) cbEtat.getSelectedItem();
        String type = tfType.getText().trim();

        // Vérification si les champs nécessaires sont remplis
        if (modele.isEmpty() || marque.isEmpty() || annee.isEmpty() || prix.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !");
            return;
        }

        // Validation de l'année et du prix
        int anneeValue;
        double prixValue;
        try {
            anneeValue = Integer.parseInt(annee);
            if (anneeValue < 1900 || anneeValue > 2100) {
                JOptionPane.showMessageDialog(this, "L'année doit être comprise entre 1900 et 2100 !");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'année doit être un nombre valide !");
            return;
        }

        try {
            prixValue = Double.parseDouble(prix);
            if (prixValue <= 0) {
                JOptionPane.showMessageDialog(this, "Le prix doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide !");
            return;
        }

        // Vérification des listes déroulantes
        if (carburant == null || etat == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une valeur valide pour le carburant et l'état !");
            return;
        }

        // Récupération sécurisée de l'ID du véhicule à partir de la ligne sélectionnée
        int idVehicule = -1;  // Valeur par défaut invalide
        try {
            idVehicule = (int) tableModel.getValueAt(selectedRow, 0);  // ID supposé en première colonne
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'ID du véhicule.");
            return;
        }

        // Connexion à la base de données et mise à jour du véhicule
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE vehicule SET modele = ?, marque = ?, annee = ?, carburant = ?, prix_location_jour = ?, etat = ?, type = ? WHERE id_vehicule = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, modele);
                stmt.setString(2, marque);
                stmt.setInt(3, anneeValue);
                stmt.setString(4, carburant);
                stmt.setDouble(5, prixValue);
                stmt.setString(6, etat);
                stmt.setString(7, type);
                stmt.setInt(8, idVehicule);

                int rowsAffected = stmt.executeUpdate(); // Vérifie si une ligne a été modifiée
                if (rowsAffected == 0) {
                    JOptionPane.showMessageDialog(this, "Aucune modification effectuée : l'ID du véhicule n'existe pas !");
                    return;
                }

                JOptionPane.showMessageDialog(this, "Véhicule modifié avec succès !");
                loadVehicules(); // Rafraîchir la table après modification
                clearFields();   // Réinitialiser les champs (si cette méthode existe)
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification du véhicule : " + e.getMessage());
            e.printStackTrace();
        }
    }


    //supprimer un véhicule
    private void supprimerVehicule() {
        int selectedRow = vehiculetable.getSelectedRow();
        if (selectedRow >= 0) {
            int idVehicule = (int) tableModel.getValueAt(selectedRow, 0); // Récupérer l'id_vehicule

            try (Connection conn = DBConnection.getConnection()) {
                String query = "DELETE FROM vehicule WHERE id_vehicule = ?";
                try (var stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, idVehicule);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Véhicule supprimé avec succès!");
                    loadVehicules();  // Rafraîchir la liste des véhicules
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du véhicule.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un véhicule à supprimer.");
        }
    }

    // Rechercher des véhicules
    private void rechercherVehicule(String marque, String carburant, String etat) {
        // Vider le tableau de résultats avant de le remplir
        rechercheTableModel.setRowCount(0);

        // Parcourir les véhicules dans tableModel et appliquer les filtres
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String vehiculeMarque = String.valueOf(tableModel.getValueAt(i, 2)); // Colonne Marque
            String vehiculeCarburant = String.valueOf(tableModel.getValueAt(i, 4)); // Colonne Carburant
            String vehiculeEtat = String.valueOf(tableModel.getValueAt(i, 6)); // Colonne État

            // Appliquer les filtres de manière sécurisée
            boolean marqueOk = (marque == null || marque.isEmpty() || vehiculeMarque.toLowerCase().contains(marque.toLowerCase()));
            boolean carburantOk = (carburant == null || carburant.equals("Tous") || carburant.equalsIgnoreCase(vehiculeCarburant));
            boolean etatOk = (etat == null || etat.equals("Tous") || etat.equalsIgnoreCase(vehiculeEtat));

            if (marqueOk && carburantOk && etatOk) {
                // Ajouter le véhicule filtré dans le tableau de résultats
                rechercheTableModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0), // ID
                        tableModel.getValueAt(i, 1), // Modèle
                        tableModel.getValueAt(i, 2), // Marque
                        tableModel.getValueAt(i, 3), // Année
                        tableModel.getValueAt(i, 4), // Carburant
                        tableModel.getValueAt(i, 5), // Prix/jour
                        tableModel.getValueAt(i, 6), // État
                        tableModel.getValueAt(i, 7)  // Type
                });
            }
        }

        // Si aucun résultat, afficher un message à l'utilisateur
        if (rechercheTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun véhicule trouvé avec les filtres spécifiés.");
        }
    }

    // Réinitialiser les champs du formulaire
    private void clearFields() {
        tfModele.setText("");
        tfMarque.setText("");
        tfAnnee.setText("");
        cbCarburant.setSelectedIndex(0);
        tfPrix.setText("");
        cbEtat.setSelectedIndex(0);
        tfType.setText("");
    }
    private void loadVehicules() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM vehicule";
            try (var stmt = conn.createStatement()) {
                var rs = stmt.executeQuery(query);
                tableModel.setRowCount(0);  // Vider la table avant de la remplir
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getInt("id_vehicule"),
                            rs.getString("modele"),
                            rs.getString("marque"),
                            rs.getInt("annee"),
                            rs.getString("carburant"),
                            rs.getDouble("prix_location_jour"),
                            rs.getString("etat"),
                            rs.getString("type"),
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GestionVehicule gestionVehicule = new GestionVehicule();
                gestionVehicule.setVisible(true);
                gestionVehicule.loadVehicules();  // Charger les véhicules à l'initialisation
            }
        });

    }}