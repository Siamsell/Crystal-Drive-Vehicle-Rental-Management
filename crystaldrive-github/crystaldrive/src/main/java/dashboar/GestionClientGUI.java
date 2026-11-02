package dashboar;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestionClientGUI extends JFrame {

    // Constructeur
    public GestionClientGUI() {
        setTitle("Gestion des Clients");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        // Initialiser les composants
        initComponents();
    }

    // Méthode pour initialiser les composants
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet Enregistrement
        JPanel clientFormPanel = createClientFormPanel();
        tabbedPane.addTab("Enregistrement", clientFormPanel);

        // Onglet Recherche
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("Recherche", searchPanel);

        // Ajouter les onglets au JFrame
        add(tabbedPane);
    }

    // Créer le panneau pour le formulaire client
    private JPanel createClientFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon(getClass().getResource("/images/img_3.png")).getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        formPanel.setLayout(new BorderLayout());

        // Panneau central pour le formulaire
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Enregistrement Client");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);  // Remplacer bleu par noir
        titleLabel.setBounds(80, 20, 300, 40);
        centerPanel.add(titleLabel);

        // Champs du formulaire
        JLabel[] labels = {new JLabel("Nom:"), new JLabel("Prénom:"), new JLabel("Email:"),
                new JLabel("Téléphone:"), new JLabel("Numéro Permis:")};
        Font font = new Font("Arial",Font.BOLD, 13);  // Police Arial, taille 16
        for (JLabel label : labels) {
            label.setFont(font);
        }

        // Initialiser les champs de texte
        JTextField[] fields = {new JTextField(), new JTextField(), new JTextField(),
                new JTextField(), new JTextField(30)};
        int y = 80;
        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(50, y, 100, 30);
            centerPanel.add(labels[i]);

            fields[i].setBounds(150, y, 200, 30);
            makeTextField(fields[i]);  // Rendre les JTextField circulaires
            centerPanel.add(fields[i]);
            y += 50;
        }

        // Bouton Enregistrer avec couleur bleue plus claire
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBounds(150, 340, 120, 40);
        saveButton.setBackground(new Color(210, 213, 221)); // Bleu plus clair
        saveButton.setForeground(Color.BLACK);
        centerPanel.add(saveButton);

        // Action du bouton Enregistrer
        saveButton.addActionListener(e -> {
            String nom = fields[0].getText();
            String prenom = fields[1].getText();
            String email = fields[2].getText();
            String telephone = fields[3].getText();
            String permis = fields[4].getText();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || permis.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                return;
            }
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer une adresse email valide.");
                return;
            }
            if (!isValidPhone(telephone)) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro de téléphone valide.");
                return;
            }

            // Enregistrer le client dans la base de données
            Client client = new Client(0, nom, prenom, telephone, email, permis);
            ClientDAO.ajouterClient(client);
           // JOptionPane.showMessageDialog(null, "Client enregistré avec succès !");
        });

        // Panneau latéral (EAST)
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(new Color(219, 225, 228)); // Couleur bleue
        eastPanel.setPreferredSize(new Dimension(200, 0)); // Largeur fixe
        eastPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("<html>Merci de faire confiance à notre système de gestion des clients et des véhicules !<br>Bienvenue cher client.</html>");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 28));  // Taille de la police ajustée
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Centrer horizontalement
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);    // Centrer verticalement
        welcomeLabel.setOpaque(false);  // Le label reste transparent
        eastPanel.setLayout(new BorderLayout()); // S'assurer que le layout du panneau est BorderLayout

        eastPanel.add(welcomeLabel, BorderLayout.CENTER);  // Ajouter le JLabel au centre de eastPanel

        // Ajouter les panneaux au panneau principal
        formPanel.add(centerPanel, BorderLayout.CENTER);
        formPanel.add(eastPanel, BorderLayout.EAST);

        return formPanel;
    }

    private void makeTextField(JTextField textField) {
        textField.setBackground(new Color(255, 255, 255));
        textField.setCaretColor(Color.BLACK);  // Pour le curseur
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setOpaque(false);


        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));


        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        textField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
    }


    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = new ImageIcon(getClass().getResource("/images/image.png")).getImage();
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        searchPanel.setLayout(new BorderLayout());

        // Barre de recherche
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBarPanel.setOpaque(false);

        // Champ de texte de recherche
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        // Boutons de recherche
        JButton searchClientButton = new JButton("Rechercher Client");
        searchClientButton.setBackground(new Color(50, 61, 83));
        searchClientButton.setForeground(Color.WHITE);

        JButton searchLocationButton = new JButton("Rechercher Location");
        searchLocationButton.setBackground(new Color(50, 61, 83));
        searchLocationButton.setForeground(Color.WHITE);

        // Bouton pour afficher tout
        JButton showAllButton = new JButton("Afficher Tout");
        showAllButton.setBackground(new Color(50, 61, 83));
        showAllButton.setForeground(Color.WHITE);

        // Ajouter les composants à la barre de recherche
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchClientButton);
        searchBarPanel.add(searchLocationButton);
        searchBarPanel.add(showAllButton);
        searchPanel.add(searchBarPanel, BorderLayout.NORTH);

        // Table des résultats pour les clients
        String[] clientColumns = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Numéro Permis"};
        JTable clientResultsTable = new JTable(new DefaultTableModel(clientColumns, 0));
        JScrollPane clientScrollPane = new JScrollPane(clientResultsTable);
        searchPanel.add(clientScrollPane, BorderLayout.CENTER);

        // Table des résultats pour les locations
        String[] locationColumns = {"ID Réservation", "Véhicule", "Date Début", "Date Fin", "Montant Total", "Statut"};
        JTable locationResultsTable = new JTable(new DefaultTableModel(locationColumns, 0));
        JScrollPane locationScrollPane = new JScrollPane(locationResultsTable);
        searchPanel.add(locationScrollPane, BorderLayout.SOUTH);

        // Action du bouton "Rechercher Client"
        searchClientButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un critère de recherche !");
                return;
            }
            List<Client> clients = ClientDAO.rechercherClients(query);
            DefaultTableModel clientModel = (DefaultTableModel) clientResultsTable.getModel();
            clientModel.setRowCount(0); // Effacer les anciennes lignes
            for (Client client : clients) {
                clientModel.addRow(new Object[]{
                        client.getId(), client.getNom(), client.getPrenom(),
                        client.getEmail(), client.getTelephone(), client.getPermis()
                });
            }
        });

        // Action du bouton "Rechercher Location"
        searchLocationButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un critère de recherche !");
                return;
            }
            List<Location> locations = ClientDAO.rechercherLocations(query);
            DefaultTableModel locationModel = (DefaultTableModel) locationResultsTable.getModel();
            locationModel.setRowCount(0); // Effacer les anciennes lignes
            for (Location location : locations) {
                locationModel.addRow(new Object[]{
                        location.getIdReservation(), location.getVehiculeDetails(),
                        location.getDateDebut(), location.getDateFin(),
                        location.getMontantTotal(), location.getStatut()
                });
            }
        });

        // Action du bouton "Afficher Tout"
        showAllButton.addActionListener(e -> {
            // Afficher tous les clients
            List<Client> allClients = ClientDAO.getAllClients();
            DefaultTableModel clientModel = (DefaultTableModel) clientResultsTable.getModel();
            clientModel.setRowCount(0); // Effacer les anciennes lignes
            for (Client client : allClients) {
                clientModel.addRow(new Object[]{
                        client.getId(), client.getNom(), client.getPrenom(),
                        client.getEmail(), client.getTelephone(), client.getPermis()
                });
            }

            // Afficher toutes les locations
            List<Location> allLocations = ClientDAO.rechercherLocations(""); // Requête vide pour tout récupérer
            DefaultTableModel locationModel = (DefaultTableModel) locationResultsTable.getModel();
            locationModel.setRowCount(0); // Effacer les anciennes lignes
            for (Location location : allLocations) {
                locationModel.addRow(new Object[]{
                        location.getIdReservation(), location.getVehiculeDetails(),
                        location.getDateDebut(), location.getDateFin(),
                        location.getMontantTotal(), location.getStatut()
                });
            }
        });

        return searchPanel;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPhone(String telephone) {
        return telephone.matches("^\\d{10}$");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionClientGUI().setVisible(true));
    }
}
