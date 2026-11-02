package dashboar;

import java.math.BigDecimal;
import java.util.Date;

public class Retour {
    private int idRetour;
    private int idReservation;
    private Date dateRetour;
    private String etatRetour;
    private BigDecimal fraisSupplementaires;

    // Constructeur complet
    public Retour(int idRetour, int idReservation, Date dateRetour, String etatRetour, BigDecimal fraisSupplementaires) {
        this.idRetour = idRetour;
        this.idReservation = idReservation;
        this.dateRetour = dateRetour;
        this.etatRetour = etatRetour;
        this.fraisSupplementaires = fraisSupplementaires;
    }

    // Constructeur sans idRetour pour insertion
    public Retour(int idReservation, Date dateRetour, String etatRetour, BigDecimal fraisSupplementaires) {
        this.idReservation = idReservation;
        this.dateRetour = dateRetour;
        this.etatRetour = etatRetour;
        this.fraisSupplementaires = fraisSupplementaires;
    }

    // Getters et setters
    public int getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(int idRetour) {
        this.idRetour = idRetour;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getEtatRetour() {
        return etatRetour;
    }

    public void setEtatRetour(String etatRetour) {
        this.etatRetour = etatRetour;
    }

    public BigDecimal getFraisSupplementaires() {
        return fraisSupplementaires;
    }

    public void setFraisSupplementaires(BigDecimal fraisSupplementaires) {
        this.fraisSupplementaires = fraisSupplementaires;
    }
}
