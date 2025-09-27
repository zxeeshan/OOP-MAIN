package model;

public class Login {
    private String username;
    protected String password;
    private User user;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Methods from class diagram
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        this.user = null;
    }

    public void setPasswordFromRaw(String rawPassword) {
        this.password = rawPassword; // In real app, this would be hashed
    }

    public boolean verifyPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }
}
