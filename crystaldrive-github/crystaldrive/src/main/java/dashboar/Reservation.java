package dashboar;

import java.util.Date;
public class Reservation {
    private String vehicle;
    private String clientName;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private String status;

    // Constructeur avec 6 arguments
    public Reservation(String vehicle, String clientName, Date startDate, Date endDate, double totalCost, String status) {
        this.vehicle = vehicle;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
    }

    // Getters
    public String getVehicle() {
        return vehicle;
    }

    public String getClientName() {
        return clientName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    // Méthode pour modifier le statut
    public void setStatus(String status) {
        this.status = status;
    }
}
