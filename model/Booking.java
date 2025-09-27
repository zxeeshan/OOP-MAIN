package model;

import java.time.LocalDate;

public class Booking {
    private int bookingID;
    private LocalDate bookingDate;
    private String startTime;
    private String endTime;
    private String status;
    private Vehicle vehicle;
    private User user;

    public Booking(int bookingID, Vehicle vehicle, String startTime, String endTime) {
        this.bookingID = bookingID;
        this.bookingDate = LocalDate.now();
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "PENDING";
        this.vehicle = vehicle;
    }

    // Getters and Setters
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Methods from class diagram
    public void reserveVehicle() {
        if (vehicle.isAvailabilityStatus()) {
            vehicle.updateAvailability(false);
            this.status = "CONFIRMED";
        }
    }

    public void cancelBooking() {
        vehicle.updateAvailability(true);
        this.status = "CANCELLED";
    }

    public void completeBooking() {
        vehicle.updateAvailability(true);
        this.status = "COMPLETED";
    }
}
