package dashboar;

public class ReservationSuivi {
    private int idReservation;
    private String marque;
    private String modele;
    private String clientNom;
    private String clientPrenom;
    private String dateDebut;
    private String dateFin;
    private double montantTotal;
    private String statut;

    // Constructeur par défaut
    public ReservationSuivi() {}

    // Constructeur avec paramètres
    public ReservationSuivi(int idReservation, String marque, String modele, String clientNom, String clientPrenom,
                            String dateDebut, String dateFin, double montantTotal, String statut) {
        this.idReservation = idReservation;
        this.marque = marque;
        this.modele = modele;
        this.clientNom = clientNom;
        this.clientPrenom = clientPrenom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantTotal = montantTotal;
        this.statut = statut;
    }

    // Getters et setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getClientPrenom() {
        return clientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}





