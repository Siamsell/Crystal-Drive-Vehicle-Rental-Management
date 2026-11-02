package dashboar;

public class Vehicule {
    private int idVehicule;
    private String modele;
    private String marque;
    private int annee;
    private String carburant;
    private double prixLocationJour;
    private String etat;
    private String type;

    public Vehicule(int idVehicule, String modele, String marque, int annee,
                    String carburant, double prixLocationJour, String etat, String type) {
        this.idVehicule = idVehicule;
        this.modele = modele;
        this.marque = marque;
        this.annee = annee;
        this.carburant = carburant;
        this.prixLocationJour = prixLocationJour;
        this.etat = etat;
        this.type = type;
    }

    // Getters et setters
    public int getIdVehicule() { return idVehicule; }
    public void setIdVehicule(int idVehicule) { this.idVehicule = idVehicule; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public String getCarburant() { return carburant; }
    public void setCarburant(String carburant) { this.carburant = carburant; }

    public double getPrixLocationJour() { return prixLocationJour; }
    public void setPrixLocationJour(double prixLocationJour) { this.prixLocationJour = prixLocationJour; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

