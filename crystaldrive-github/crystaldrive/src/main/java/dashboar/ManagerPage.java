package dashboar;

import javax.swing.*;
import java.awt.*;

public class ManagerPage extends JFrame {
    public ManagerPage() {
        // Configuration de la fenêtre principale
        setTitle("Page Manager - Location de Véhicules");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Définir une couleur de fond pour la fenêtre principale
        getContentPane().setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(4, 39, 54)); // Bleu élégant
        JLabel headerLabel = new JLabel("Bienvenue Cher Client - Location de Véhicules");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE); // Texte blanc
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Menu principal
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        menuPanel.setBackground(new Color(62, 120, 144)); // Couleur de fond neutre

        JButton btnReservationManagement = createButton("Gestion des Réservations", new Color(101, 49, 49, 195), "src/paiement.png");
        JButton btnPaymentRecords = createButton("Paiements & Acomptes", new Color(101, 49, 49, 195), "src/reserve.png");

        menuPanel.add(btnReservationManagement);
        menuPanel.add(btnPaymentRecords);
        add(menuPanel, BorderLayout.CENTER);

        // Actions des boutons
        btnReservationManagement.addActionListener(e -> navigateTo("Gestion des Réservations"));
        btnPaymentRecords.addActionListener(e -> navigateTo("Paiements & Acomptes"));

        setVisible(true);
    }

    // Méthode pour créer un bouton avec style
    private JButton createButton(String text, Color backgroundColor, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false); // Supprimer la bordure de focus
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes
        button.setPreferredSize(new Dimension(250, 150));

        // Ajout de l'icône
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Redimensionner l'icône
            button.setIcon(new ImageIcon(img));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'icône : " + iconPath);
        }

        // Coins arrondis
        button.setBorder(BorderFactory.createLineBorder(backgroundColor.darker(), 2));
        button.setContentAreaFilled(true);
        return button;
    }

    // Méthode de navigation vers des pages ou fonctionnalités spécifiques
    private void navigateTo(String section) {
        switch (section) {

            case "Gestion des Réservations":
                new InterfaceReservation();
                break;

            case"Paiements & Acomptes":
                new InterfaceReservation();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Section inconnue : " + section);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagerPage::new);
    }
}
