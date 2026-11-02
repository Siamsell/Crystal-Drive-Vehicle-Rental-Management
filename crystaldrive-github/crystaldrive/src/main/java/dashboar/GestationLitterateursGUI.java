package dashboar;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.AbstractBorder;
import java.awt.geom.RoundRectangle2D;

public class GestationLitterateursGUI extends JFrame {
    private JTextField tfNom, tfPrenom, tfEmail, tfMotDePasse;
    private JComboBox<String> cbRole;
    private JButton btnAjouter, btnConnexion;


    // Connexion à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/location_vehicules?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public GestationLitterateursGUI() {
        setTitle("Créer votre compte ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Chargement de l'image de fond
        JPanel backgroundPanel = new JPanel() {
            private final Image backgroundImage = new ImageIcon(getClass().getResource("/images/lol.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Erreur : L'image de fond n'a pas pu être chargée !");
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout(15, 15));

        // Police et couleurs
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBold = new Font("Segoe UI", Font.BOLD, 14);
        Color textColor = Color.white;

        // Panneau de saisie
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Nom
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setFont(fontBold);
        lblNom.setForeground(Color.white);
        tfNom = new JTextField();
        tfNom.setFont(font);
        tfNom.setPreferredSize(new Dimension(250, 30));
        tfNom.setBorder(new RoundedBorder(10, Color.gray));
        tfNom.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblNom, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(tfNom, gbc);


        // Prénom
        JLabel lblPrenom = new JLabel("Prénom:");
        lblPrenom.setFont(fontBold);
        lblPrenom.setForeground(Color.white);
        tfPrenom = new JTextField();
        tfPrenom.setFont(font);
        tfPrenom.setOpaque(false);
        tfPrenom.setBorder(new RoundedBorder(10, Color.gray));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblPrenom, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(tfPrenom, gbc);
        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(fontBold);
        lblEmail.setForeground(Color.white);
        tfEmail = new JTextField();
        tfEmail.setFont(font);
        tfEmail.setBorder(new RoundedBorder(10, Color.gray));
        tfEmail.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(tfEmail, gbc);


        // Mot de Passe

        JLabel lblMotDePasse = new JLabel("Mot de passe:");
        lblMotDePasse.setFont(fontBold);
        lblMotDePasse.setForeground(Color.white);
        tfMotDePasse = new JPasswordField(20);
        tfMotDePasse.setFont(font);
        tfMotDePasse.setBorder(new RoundedBorder(10, Color.gray));
        tfMotDePasse.setOpaque(false);

// Définir les contraintes pour le mot de passe
        gbc.gridx = 0;
        gbc.gridy = 3;  // Déplacez-le à la ligne suivante après l'email
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblMotDePasse, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(tfMotDePasse, gbc);



        // Rôle
        JLabel lblRole = new JLabel("Rôle:");
        lblRole.setFont(fontBold);
        lblRole.setForeground(Color.white);


        cbRole = new JComboBox<>(new String[]{"Administrateur", "Gestionnaire", "Client"});
        cbRole.setFont(font);
        cbRole.setOpaque(false);
        cbRole.setBorder(new RoundedBorder(10, new Color(4, 39, 54, 255)));
        gbc.gridx = 0;
        gbc.gridy = 4;
        cbRole.setForeground(Color.black);
        gbc.anchor = GridBagConstraints.LINE_END;
        panelForm.add(lblRole, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panelForm.add(cbRole, gbc);

        backgroundPanel.add(panelForm, BorderLayout.CENTER);
        tfNom.setForeground(Color.WHITE);
        tfPrenom.setForeground(Color.WHITE);
        tfEmail.setForeground(Color.WHITE);
        tfMotDePasse.setForeground(Color.WHITE);

        // Panneau des boutons
        JPanel panelButtons = new JPanel();
        panelButtons.setOpaque(false);


        panelButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        // panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Bouton Ajouter
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(fontBold);
        btnAjouter.setBackground(new Color(4, 39, 54, 255)); // Couleur opaque sans transparence

        btnAjouter.setForeground(textColor);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setIconTextGap(10);  // Espacement entre l'icône et le texte
        btnAjouter.setPreferredSize(new Dimension(150, 50));  // Taille du bouton
        btnAjouter.setHorizontalTextPosition(SwingConstants.RIGHT); // L'icône à gauche du texte
        btnAjouter.setVerticalTextPosition(SwingConstants.CENTER); // Texte centré verticalement
        btnAjouter.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/ajt.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))); // Icône petite
        btnAjouter.setBorder(new RoundedBorder(15, Color.GRAY));
        btnAjouter.addActionListener(e -> ajouterUtilisateur());

        // Bouton Connexion
        btnConnexion = new JButton("Connexion");btnConnexion.setBorder(new RoundedBorder(15, Color.GRAY));

        btnConnexion.setFont(fontBold);
        btnConnexion.setBackground(new Color(4, 39, 54, 255));
        btnConnexion.setForeground(textColor);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setIconTextGap(10);  // Espacement entre l'icône et le texte
        btnConnexion.setPreferredSize(new Dimension(150, 50));  // Taille du bout// Taille du bout
        btnConnexion.setHorizontalTextPosition(SwingConstants.RIGHT); // L'icône à gauche du texte
        btnConnexion.setVerticalTextPosition(SwingConstants.CENTER); // Texte centré verticalement
        btnConnexion.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/connecte.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))); // Icône petite
        btnConnexion.addActionListener(e -> ouvrirFenetreConnexion());
        panelButtons.add(btnAjouter);
        panelButtons.add(btnConnexion);
        backgroundPanel.add(panelButtons, BorderLayout.SOUTH);


        // Zone de texte pour afficher les résultats

// Ajouter le panneau de fond à la fenêtre
        setContentPane(backgroundPanel);
    }

    private boolean isValidEmail(String email) {
        // Expression régulière pour valider l'email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private void ajouterUtilisateur() {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String email = tfEmail.getText();
        String motDePasse = tfMotDePasse.getText();
        String role = (String) cbRole.getSelectedItem();

        // Vérification que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || role == null || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Erreur : Il faut remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification du format de l'email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Erreur : L'email n'est pas valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérification si l'email existe déjà dans la base de données
        String checkQuery = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        String insertQuery = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Erreur : Un compte avec cet email existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, nom);
                insertStmt.setString(2, prenom);
                insertStmt.setString(3, email);
                insertStmt.setString(4, motDePasse);
                insertStmt.setString(5, role);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void ouvrirFenetreConnexion() {
        // Créer la fenêtre de connexion
        JFrame connexionFrame = new JFrame("Connexion");
        connexionFrame.setSize(400, 300);
        connexionFrame.setLocationRelativeTo(null);
        connexionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Charger l'image d'arrière-plan
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/images/lol.png")); // Remplacez par le chemin de votre image
        Image backgroundImage = backgroundIcon.getImage();

        // Panneau personnalisé pour l'arrière-plan
        JPanel panelBackground = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Erreur : L'image de fond n'a pas pu être chargée !");
                }
            }
        };

        panelBackground.setLayout(new BorderLayout());
        // Panneau principal
        JPanel panelConnexion = new JPanel();
        panelConnexion.setLayout(new GridLayout(3, 2, 10, 10));
        panelConnexion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelConnexion.setOpaque(false);
        // Email et mot de passe
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setHorizontalAlignment(SwingConstants.RIGHT); // Alig
        JLabel lblMotDePasse = new JLabel("Mot de passe:");
        lblMotDePasse.setForeground(Color.WHITE);


        JTextField tfEmail = new JTextField();
        tfEmail.setOpaque(false);

        tfEmail.setForeground(Color.WHITE);
        tfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfEmail.setBorder(new RoundedBorder(10, Color.WHITE));
        tfEmail.setPreferredSize(new Dimension(80, 25));


        JPasswordField tfMotDePasse = new JPasswordField();
        tfMotDePasse.setOpaque(false); // Transparence
        tfMotDePasse.setForeground(Color.WHITE);
        tfMotDePasse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfMotDePasse.setBorder(new RoundedBorder(10, Color.WHITE));
        tfMotDePasse.setPreferredSize(new Dimension(80, 25));

        lblMotDePasse.setHorizontalAlignment(SwingConstants.RIGHT);
        panelConnexion.add(lblEmail);
        panelConnexion.add(tfEmail);
        panelConnexion.add(lblMotDePasse);
        panelConnexion.add(tfMotDePasse);

        // Bouton de connexion avec icône
        JButton btnSeConnecter = new JButton("Se connecter");
        btnSeConnecter.setContentAreaFilled(false);
        btnSeConnecter.setPreferredSize(new Dimension(100, 30));
        btnSeConnecter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeConnecter.setBorder(new RoundedBorder(20, Color.black));
       btnSeConnecter.setBackground(new Color(4, 39, 54, 255)); // Fond bleu
        btnSeConnecter.setOpaque(true);
        btnSeConnecter.setForeground(Color.white);

        btnSeConnecter.addActionListener(e -> {
            String email = tfEmail.getText().trim();  // Récupérer le texte de l'email et enlever les espaces superflus
            String motDePasse = tfMotDePasse.getText().trim();  // Récupérer le texte du mot de passe
            System.out.println("Email envoyé: '" + email + "'");
            System.out.println("Mot de passe envoyé: '" + motDePasse + "'");
            // Passer les valeurs à la méthode de connexion
            seConnecter(email, motDePasse, connexionFrame);
        });
        btnSeConnecter.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/connecte.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))); // Icône p
        panelConnexion.add(new JLabel());  // Empty cell for layout
        panelConnexion.add(btnSeConnecter);
        panelBackground.add(panelConnexion, BorderLayout.CENTER);
        // Ajouter le panneau à la fenêtre de connexion
        btnSeConnecter.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        connexionFrame.add(panelBackground);

        connexionFrame.setVisible(true);
    }

    private void seConnecter(String email, String motDePasse, JFrame connexionFrame) {
        // Vérifier que les champs sont non vides
        System.out.println("Email: '" + email + "'");
        System.out.println("Mot de Passe: '" + motDePasse + "'");

        if (email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(connexionFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Si un des champs est vide, on arrête l'exécution de la méthode
        }

        // Requête pour vérifier l'email et le mot de passe dans la base de données
        String query = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Remplacer les paramètres de la requête
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Si l'utilisateur est trouvé, afficher un message de bienvenue
                String nom = rs.getString("nom");
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(connexionFrame, "Connexion réussie ! Bienvenue, " + nom, "Succès", JOptionPane.INFORMATION_MESSAGE);
                connexionFrame.dispose();

                // Ouvrir l'interface selon le rôle
                switch (role.toLowerCase()) {
                    case "client":
                        ouvrirGestionClientGui(); // Méthode pour afficher l'interface client
                        break;
                    case "administrateur":
                        ouvrirFenetreAdmin(); // Méthode pour afficher l'interface manager
                        break;
                    case "gestionnaire":
                        ouvrirFenetregestionnaire(); // Méthode pour afficher l'interface admin
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Rôle inconnu : " + role, "Erreur", JOptionPane.ERROR_MESSAGE);
                        break;
                }
                // Rediriger l'utilisateur vers l'interface appropriée
            } else {
                // Si aucune correspondance n'est trouvée, afficher un message d'erreur
                JOptionPane.showMessageDialog(connexionFrame, "Erreur : Email ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(connexionFrame, "Erreur lors de la tentative de connexion.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void ouvrirGestionClientGui() {
     new clientapp().setVisible(true);
    }
    private void ouvrirFenetreAdmin() {
        new AdminDashboardGUI().setVisible(true);
    }

    private void ouvrirFenetregestionnaire() {
      new GestionnairePage().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestationLitterateursGUI().setVisible(true));
    }


    // Classe pour une bordure arrondie
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 4, 4, 4);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

    }
}