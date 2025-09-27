package model;

import java.util.List;

public class Admin {
    private int adminID;
    private String name;
    private Login login;

    public Admin(int adminID, String name) {
        this.adminID = adminID;
        this.name = name;
    }

    // Getters and Setters
    public int getAdminID() { return adminID; }
    public void setAdminID(int adminID) { this.adminID = adminID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Login getLogin() { return login; }
    public void setLogin(Login login) { this.login = login; }

    // Methods from class diagram
    public void manageVehicles(List<Vehicle> vehicles) {
        // Implementation for vehicle management
        System.out.println("Managing vehicles...");
    }

    public void manageUsers(List<User> users) {
        // Implementation for user management
        System.out.println("Managing users...");
    }

    public void viewSystemLogs() {
        // Implementation for viewing system logs
        System.out.println("Viewing system logs...");
    }
}
