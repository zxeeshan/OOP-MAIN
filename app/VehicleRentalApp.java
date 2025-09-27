package app;
import frontend.AdminDashboardPanel;
import frontend.LoginPanel;
import frontend.UserDashboardPanel;
import java.awt.*;
import javax.swing.*;

public class VehicleRentalApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private UserDashboardPanel userDashboard;
    private AdminDashboardPanel adminDashboard;

    public VehicleRentalApp() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Vehicle Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new LoginPanel(this);
        userDashboard = new UserDashboardPanel(this);
        adminDashboard = new AdminDashboardPanel(this);
    }

    private void setupLayout() {
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(userDashboard, "USER_DASHBOARD");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");

        add(mainPanel);
    }

    private void setupEventHandlers() {
        // Event handlers will be set up in individual panels
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showUserDashboard() {
        cardLayout.show(mainPanel, "USER_DASHBOARD");
    }

    public void showAdminDashboard() {
        cardLayout.show(mainPanel, "ADMIN_DASHBOARD");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new VehicleRentalApp().setVisible(true);
        });
    }
}
