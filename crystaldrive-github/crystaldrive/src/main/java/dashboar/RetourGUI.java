package dashboar;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;

public class RetourGUI extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image background = new ImageIcon("src/img_1.png").getImage();
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    public RetourGUI() {
        setLayout(null);

        // Titre de l'interface
        JLabel titleLabel = new JLabel("Retour Véhicule", JLabel.CENTER);
        titleLabel.setBounds(0, 150, 800, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // ID Réservation
        JLabel reservationLabel = new JLabel("ID Réservation:");
        reservationLabel.setBounds(150, 250, 150, 25);
        reservationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        reservationLabel.setForeground(Color.white);
        JTextField reservationField = new JTextField();
        reservationField.setBounds(350, 250, 300, 25);

        // Date Retour
        JLabel dateLabel = new JLabel("Date Retour (yyyy-MM-dd):");
        dateLabel.setBounds(150, 300, 150, 25);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(Color.white);
        JTextField dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // Date actuelle par défaut
        dateField.setBounds(350, 300, 300, 25);

        // État Retour
        JLabel etatLabel = new JLabel("État Retour:");
        etatLabel.setBounds(150, 350, 150, 25);
        etatLabel.setForeground(Color.white);
        etatLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JComboBox<String> etatComboBox = new JComboBox<>(new String[]{"Bon état", "Dommage"});
        etatComboBox.setBounds(350, 350, 300, 25);

        // Frais Supplémentaires
        JLabel fraisLabel = new JLabel("Frais Supplémentaires:");
        fraisLabel.setBounds(150, 400, 150, 25);
        fraisLabel.setForeground(Color.white);
        fraisLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField fraisField = new JTextField("00.00");
        fraisField.setBounds(350, 400, 300, 25);

        // Ajouter un listener au JComboBox pour gérer les frais
        etatComboBox.addActionListener(e -> {
            if ("Bon état".equals(etatComboBox.getSelectedItem())) {
                fraisField.setText("00.00");
                fraisField.setEditable(false); // Rendre le champ non modifiable
            } else {
                fraisField.setEditable(true); // Permettre la modification si "Dommage" est sélectionné
                fraisField.setText(""); // Réinitialiser le champ
            }
        });

        // Bouton Enregistrer
        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBounds(200, 500, 120, 30);

        // Ajouter les composants au panneau
        add(titleLabel); // Ajouter le titre
        add(reservationLabel);
        add(reservationField);
        add(dateLabel);
        add(dateField);
        add(etatLabel);
        add(etatComboBox);
        add(fraisLabel);
        add(fraisField);
        add(saveButton);

        // Action du bouton Enregistrer
        saveButton.addActionListener(e -> {
            try {
                // Vérification des champs
                if (reservationField.getText().trim().isEmpty() ||
                        dateField.getText().trim().isEmpty() ||
                        fraisField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idReservation = Integer.parseInt(reservationField.getText().trim());
                Date dateRetour = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText().trim());
                String etatRetour = (String) etatComboBox.getSelectedItem();
                BigDecimal fraisSupplementaires = new BigDecimal(fraisField.getText().trim());

                // Mise à jour de l'état du véhicule
                String nouvelEtat = etatRetour.equals("Bon état") ? "Disponible" : "Maintenance";

                // Afficher un résumé des actions
                String message = "ID Réservation : " + idReservation +
                        "\nDate de Retour : " + new SimpleDateFormat("yyyy-MM-dd").format(dateRetour) +
                        "\nÉtat Retour : " + etatRetour +
                        "\nFrais Supplémentaires : " + fraisSupplementaires +
                        "\nNouvel État : " + nouvelEtat;

                int confirmation = JOptionPane.showConfirmDialog(this, message + "\nConfirmez-vous ces informations ?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    // Créer un objet Retour et appeler la DAO pour l'ajouter
                    Retour retour = new Retour(idReservation, dateRetour, etatRetour, fraisSupplementaires);
                    new RetourDAO().ajouterRetour(retour);

                    // Mettre à jour l'état du véhicule dans la table `vehicule`
                    new RetourDAO().mettreAJourEtat(idReservation, nouvelEtat);

                    JOptionPane.showMessageDialog(this, "Retour enregistré avec succès et état mis à jour !");

                    // Réinitialiser les champs
                    reservationField.setText("");
                    dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    fraisField.setText("00.00");
                    fraisField.setEditable(false);
                    etatComboBox.setSelectedIndex(0);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le format des données est incorrect. Veuillez vérifier les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    // Méthode principale pour afficher l'interface utilisateur
    public static void main(String[] args) {
        // Créer une fenêtre principale
        JFrame frame = new JFrame("Gestion des Retours");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700); // Taille ajustée pour afficher tous les composants
        frame.setLocationRelativeTo(null); // Centrer la fenêtre

        // Ajouter le panneau RetourGUI
        frame.add(new RetourGUI());

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }
}
