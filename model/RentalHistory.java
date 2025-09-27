package model;

import java.time.LocalDate;

public class RentalHistory {
    private int bookingID;
    private int userID;
    private int vehicleID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public RentalHistory(int bookingID, int userID, int vehicleID, LocalDate startDate, LocalDate endDate, String status) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.vehicleID = vehicleID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Methods from class diagram
    public boolean filterByUser(int userID) {
        return this.userID == userID;
    }

    public boolean filterByVehicle(int vehicleID) {
        return this.vehicleID == vehicleID;
    }

    public boolean filterByDate(LocalDate startDate, LocalDate endDate) {
        return !this.startDate.isAfter(endDate) && !this.endDate.isBefore(startDate);
    }
}
