package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String name;
    private String email;
    private String phone;
    private Login login;
    private List<Booking> bookings;
    private List<RentalHistory> rentalHistory;

    public User(int userID, String name, String email, String phone) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bookings = new ArrayList<>();
        this.rentalHistory = new ArrayList<>();
    }

    // Getters and Setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Login getLogin() { return login; }
    public void setLogin(Login login) { this.login = login; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public List<RentalHistory> getRentalHistory() { return rentalHistory; }
    public void setRentalHistory(List<RentalHistory> rentalHistory) { this.rentalHistory = rentalHistory; }

    // Methods from class diagram
    public Booking makeBooking(Vehicle vehicle, String startTime, String endTime) {
        Booking booking = new Booking(bookings.size() + 1, vehicle, startTime, endTime);
        bookings.add(booking);
        return booking;
    }

    public List<RentalHistory> viewRentalHistory() {
        return rentalHistory;
    }
}
