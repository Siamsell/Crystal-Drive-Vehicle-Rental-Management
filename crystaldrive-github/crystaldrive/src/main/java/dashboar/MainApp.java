package dashboar;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    public MainApp() {
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
        JButton openRetourGUIButton = new JButton("Ouvrir RetourGUI");
        openRetourGUIButton.setFont(new Font("Arial", Font.BOLD, 16));
        openRetourGUIButton.setForeground(Color.WHITE); // Texte blanc
        openRetourGUIButton.setBackground(new Color(4, 39, 54)); // Bleu moderne
        openRetourGUIButton.setFocusPainted(false); // Supprimer la bordure de focus
        openRetourGUIButton.setBorder(BorderFactory.createLineBorder(new Color(33, 73, 91, 255), 2)); // Bordure fine
        openRetourGUIButton.setPreferredSize(new Dimension(200, 50)); // Taille ajustée

        // Action du bouton
        openRetourGUIButton.addActionListener(e -> {
            // Créer un JFrame pour afficher RetourGUI
            JFrame retourFrame = new JFrame("Retour Véhicule");
            retourFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            retourFrame.setSize(800, 700);
            retourFrame.setLocationRelativeTo(null);

            // Ajouter RetourGUI au JFrame
            retourFrame.add(new RetourGUI());

            // Afficher la nouvelle fenêtre
            retourFrame.setVisible(true);
        });

        // Ajout du bouton au panel principal
        mainPanel.add(openRetourGUIButton);

        // Ajouter le panel principal à la fenêtre
        add(mainPanel, BorderLayout.CENTER);

        // Rendre la fenêtre visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
