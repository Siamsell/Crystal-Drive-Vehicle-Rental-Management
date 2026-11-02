package dashboar;
import javax.swing.*;
import java.awt.*;

public class CrystalDriveMainPage extends JFrame {
    public CrystalDriveMainPage() {
        // Configuration de la fenêtre principale
        setTitle("Crystal Drive - Location de Véhicules");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo et en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/images/crystal drive.png"))
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH))); // Image redimensionnée en cercle
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Ajout de marges
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        headerPanel.setBackground(new Color(4, 57, 52));
        add(headerPanel, BorderLayout.NORTH);

        // Contenu principal
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE); // Fond clair et épuré

        // Carrousel dynamique des véhicules avec animation
        JPanel carSliderPanel = createCarSliderPanel();
        mainContentPanel.add(carSliderPanel, BorderLayout.CENTER);

        // Section des promotions
        JPanel promotionsPanel = createPromotionsPanel();
        mainContentPanel.add(promotionsPanel, BorderLayout.SOUTH);

        // Panel d'histoire à droite
        JPanel historyPanel = createHistoryPanel();
        mainContentPanel.add(historyPanel, BorderLayout.EAST);

        add(mainContentPanel, BorderLayout.CENTER);

        // Footer stylé avec une mise en page professionnelle
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        // Bouton "Se Connecter" stylé avec hover
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton connectButton = createConnectButton();
        buttonPanel.add(connectButton);
        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Méthode pour créer le panel de carrousel des véhicules
    private JPanel createCarSliderPanel() {
        JPanel carSliderPanel = new JPanel(new CardLayout());
        String[] carImages = {"/images/image (1).png", "/images/image (2).png", "/images/image (3).png",
                "/images/image (4).png", "/images/image (5).png", "/images/image (6).png", "/images/image (7).png"};
        for (String image : carImages) {
            JLabel carLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource(image))
                    .getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH))); // Taille des images optimisée
            carLabel.setHorizontalAlignment(SwingConstants.CENTER);
            carLabel.setOpaque(true); // Pour la transparence de l'image
            carSliderPanel.add(carLabel, image);
        }
        // Animation : Transition d'image toutes les 5 secondes
        Timer timer = new Timer(5000, e -> ((CardLayout) carSliderPanel.getLayout()).next(carSliderPanel));
        timer.start();
        return carSliderPanel;
    }

    // Méthode pour créer le panel des promotions
    private JPanel createPromotionsPanel() {
        JPanel promotionsPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // 4 annonces + 1 pour le titre
        promotionsPanel.setBackground(new Color(4, 57, 52)); // Fond foncé pour contraste

        JLabel promotionsTitle = new JLabel("Nos Offres Spéciales", SwingConstants.CENTER);
        promotionsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        promotionsTitle.setForeground(Color.WHITE);
        promotionsPanel.add(promotionsTitle);

        String[] promotions = {
                " Location de SUV à partir de 29€/jour !",
                "💎 Profitez de nos dernières promotions sur les voitures de luxe.",
                "🎉 Réservez maintenant et bénéficiez d'une réduction de 10% sur votre première location.",
                "Découvrez notre histoire ci-dessous !"
        };

        for (String promotion : promotions) {
            JLabel promotionLabel = new JLabel(promotion, SwingConstants.CENTER);
            promotionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            promotionLabel.setForeground(Color.WHITE);
            promotionsPanel.add(promotionLabel);
        }

        return promotionsPanel;
    }

    // Méthode pour créer le panel d'histoire
    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.LIGHT_GRAY); // Arrière-plan plus clair

        JLabel historyTitle = new JLabel("Notre Histoire", SwingConstants.CENTER);
        historyTitle.setFont(new Font("Arial", Font.BOLD, 18));
        historyTitle.setForeground(Color.BLACK);

        String historyText = "Crystal Drive a été fondée en 2010 et propose des services de location de véhicules de qualité. " +
                "Nous sommes spécialisés dans la location de voitures de luxe, SUV et véhicules utilitaires. " +
                "Notre objectif est de fournir des services fiables et un excellent rapport qualité-prix.";

        JTextArea historyContent = new JTextArea(historyText);
        historyContent.setFont(new Font("Arial", Font.PLAIN, 14));
        historyContent.setLineWrap(true);
        historyContent.setWrapStyleWord(true);
        historyContent.setEditable(false);
        historyContent.setBackground(Color.LIGHT_GRAY);
        historyContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyContent), BorderLayout.CENTER);

        return historyPanel;
    }

    // Méthode pour créer le panel du footer
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        footerPanel.setBackground(new Color(0, 102, 102, 150)); // Fond plus clair pour l'effet de dégradé
        footerPanel.setForeground(Color.WHITE);

        JLabel contactTitle = new JLabel("Contactez-nous", SwingConstants.CENTER);
        contactTitle.setFont(new Font("Arial", Font.BOLD, 16));
        contactTitle.setForeground(Color.WHITE);
        footerPanel.add(contactTitle);

        JLabel phoneLabel = new JLabel("Téléphone : +33 1 23 45 67 89", SwingConstants.CENTER);
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        footerPanel.add(phoneLabel);

        JLabel emailLabel = new JLabel("Email : contact@crystaldrive.com", SwingConstants.CENTER);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        footerPanel.add(emailLabel);

        return footerPanel;
    }

    // Méthode pour créer le bouton "Se Connecter"
    private JButton createConnectButton() {
        JButton connectButton = new JButton("Se Connecter ou crier votre compte");
        connectButton.setFont(new Font("Arial", Font.BOLD, 14));
        connectButton.setBackground(new Color(4, 57, 52));
        connectButton.setForeground(Color.WHITE);
        connectButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur style main
        connectButton.addActionListener(e -> navigateToLogin());
        return connectButton;
    }

    // Méthode de navigation vers la page de connexion
    private void navigateToLogin() {
        // Remplacement par le code pour ouvrir la page de gestion des utilisateurs
        new GestationLitterateursGUI().setVisible(true); // Ferme la page actuelle si nécessaire
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CrystalDriveMainPage::new);
    }
}
