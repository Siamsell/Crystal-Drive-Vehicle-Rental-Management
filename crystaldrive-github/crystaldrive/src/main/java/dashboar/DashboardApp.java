package dashboar;
import javax.swing.*;

public class DashboardApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard - Retour Véhicule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setLocationRelativeTo(null);
        frame.add(new RetourGUI());
        frame.setVisible(true);
    }


}
