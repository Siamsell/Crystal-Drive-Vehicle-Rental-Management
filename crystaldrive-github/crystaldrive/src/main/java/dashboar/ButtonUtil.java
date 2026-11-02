package dashboar;

import javax.swing.*;
import java.awt.*;

public class ButtonUtil {

    // Méthode pour créer un bouton stylisé avec des paramètres par défaut
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 150, 255)); // Couleur de fond (bleu clair)
        button.setForeground(Color.WHITE); // Texte en blanc
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Police et taille
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Marges internes
        button.setFocusPainted(false); // Supprimer la bordure de focus
        return button;
    }
}
