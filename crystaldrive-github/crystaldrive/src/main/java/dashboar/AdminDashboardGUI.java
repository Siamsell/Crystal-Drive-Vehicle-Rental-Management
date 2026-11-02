package dashboar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminDashboardGUI extends JFrame {

    public AdminDashboardGUI() {
        // Paramètres de la fenêtre principale
        this.setTitle("Tableau de Bord - Administrateur");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);

        // Configuration du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);

        // Création du menu latéral
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(34, 47, 62)); // Couleur sombre bleutée pour le menu
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(52, 73, 94))); // Bordure séparatrice

        // Création du panneau central
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(236, 240, 241)); // Couleur gris clair pour le contenu


        // Ajout des boutons au menu latéral avec navigation
        addSidebarButton(sidebar, "Gestion des clients", e -> navigateTo("Gestion des clients"));
        addSidebarButton(sidebar, "Gestion des Véhicules", e -> navigateTo("Gestion des Véhicules"));
        addSidebarButton(sidebar, "Gestion des Réservations", e -> navigateTo("Gestion des Réservations"));
        addSidebarButton(sidebar, "Rapports et Performances", e -> navigateTo("Rapports et Performances"));
        addSidebarButton(sidebar, "Historique Complet", e -> navigateTo("Historique Complet"));

        // Titre principal
        JLabel titleLabel = new JLabel("Tableau de Bord Administrateur", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(1, 15, 30)); // Couleur bleue pour le titre principal
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Ajout des composants au panneau principal
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Afficher la fenêtre
        this.setVisible(true);
    }

    /**
     * Ajoute un bouton stylisé au menu latéral.
     */
    private void addSidebarButton(JPanel sidebar, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Aligner les boutons au centre verticalement
        button.setBackground(new Color(44, 62, 80)); // Couleur bleutée foncée pour les boutons
        button.setForeground(Color.WHITE); // Texte blanc pour le contraste
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(action);

        sidebar.add(Box.createRigidArea(new Dimension(0, 20))); // Espacement entre les boutons
        sidebar.add(button);
    }

    private void navigateTo(String section) {
        switch (section) {
            case "Gestion des Véhicules":
                new MainAppp();
                break;
            case "Gestion des Réservations":
                new InterfaceReservation();
                break;
            case "Rapports et Performances":
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
            case "Historique Complet":
                new ReportingInterface();
                break;
            case "Gestion des clients":
                new  GestionClientGUI();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Section inconnue : " + section, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboardGUI::new);
    }
}
