package dashboar;

import javax.swing.*;
import java.awt.*;

public class MainAppp extends JFrame {
    public MainAppp() {
        setTitle("Application Principale");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Centrer la fenêtre
        setLocationRelativeTo(null);

        // Panel principal pour le contenu
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Couleur de fond neutre

        // Création du bouton stylisé
        JButton opengestionvehiculesButton = new JButton("Ouvrir gestion vehicules");
        opengestionvehiculesButton.setFont(new Font("Arial", Font.BOLD, 16));
        opengestionvehiculesButton.setForeground(Color.WHITE); // Texte blanc
        opengestionvehiculesButton.setBackground(new Color(4, 39, 54)); // Bleu moderne
        opengestionvehiculesButton.setFocusPainted(false); // Supprimer la bordure de focus
        opengestionvehiculesButton.setBorder(BorderFactory.createLineBorder(new Color(33, 73, 91), 2)); // Bordure fine
        opengestionvehiculesButton.setPreferredSize(new Dimension(250, 50)); // Taille ajustée

        // Action du bouton
        opengestionvehiculesButton.addActionListener(e -> {
            // Créer et afficher une nouvelle instance de GestionVehicule
            GestionVehicule gestionVehicule = new GestionVehicule();
            gestionVehicule.setVisible(true);
        });

        // Ajout du bouton au panel principal
        mainPanel.add(opengestionvehiculesButton);

        // Ajouter le panel principal à la fenêtre
        add(mainPanel, BorderLayout.CENTER);

        // Rendre la fenêtre visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainAppp::new);
    }
}
