package dashboar;

import java.math.BigDecimal;
import java.util.Date;

public class Location {
    private int idReservation;
    private String vehicule; // Contient les informations sur le véhicule
    private Date dateDebut;
    private Date dateFin;
    private BigDecimal montantTotal;
    private String statut;

    // Constructeur
    public Location(int idReservation, String vehicule, Date dateDebut, Date dateFin, BigDecimal montantTotal, String statut) {
        this.idReservation = idReservation;
        this.vehicule = vehicule;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantTotal = montantTotal;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Nouvelle méthode pour obtenir les détails du véhicule
    public String getVehiculeDetails() {
        // Retourne une description enrichie ou simplement les informations disponibles
        return "Véhicule : " + vehicule;
    }

    @Override
    public String toString() {
        return "Location{" +
                "idReservation=" + idReservation +
                ", vehicule='" + vehicule + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", montantTotal=" + montantTotal +
                ", statut='" + statut + '\'' +
                '}';
    }
}
