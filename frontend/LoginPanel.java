package frontend;

import model.*;
import javax.swing.*;


import app.VehicleRentalApp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LoginPanel extends JPanel {
    private VehicleRentalApp parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JComboBox<String> userTypeCombo;
    
    // Sample data for demonstration
    private List<User> users;
    private List<Admin> admins;

    public LoginPanel(VehicleRentalApp parent) {
        this.parent = parent;
        initializeSampleData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeSampleData() {
        users = new ArrayList<>();
        admins = new ArrayList<>();
        
        // Create sample users
        User user1 = new User(1, "John Doe", "john@email.com", "123-456-7890");
        Login userLogin1 = new Login("user1", "password");
        user1.setLogin(userLogin1);
        users.add(user1);
        
        User user2 = new User(2, "Jane Smith", "jane@email.com", "098-765-4321");
        Login userLogin2 = new Login("user2", "password");
        user2.setLogin(userLogin2);
        users.add(user2);
        
        // Create sample admin
        Admin admin1 = new Admin(1, "Admin User");
        Login adminLogin1 = new Login("admin", "admin");
        admin1.setLogin(adminLogin1);
        admins.add(admin1);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Initialize components
        userTypeCombo = new JComboBox<>(new String[]{"User", "Admin"});
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        // Style buttons
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
    }

    private void setupLayout() {
        // Title
        JLabel titleLabel = new JLabel("Vehicle Rental System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // User type selection
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("User Type:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        userTypeCombo = new JComboBox<>(new String[]{"User", "Admin"});
        formPanel.add(userTypeCombo, gbc);

        // Username field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setBackground(new Color(60, 179, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        formPanel.add(buttonPanel, gbc);

        // Demo credentials info
        gbc.gridy = 4;
        JLabel demoLabel = new JLabel("<html><center>Demo Credentials:<br/>User: user1/password or user2/password<br/>Admin: admin/admin</center></html>");
        demoLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(demoLabel, gbc);
        
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0; centerGbc.gridy = 0;
        centerPanel.add(formPanel, centerGbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });

        // Enter key support
        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKey, "login");
        getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("User".equals(userType)) {
            for (User user : users) {
                if (user.getLogin().authenticate(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user.getName(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.showUserDashboard();
                    return;
                }
            }
        } else if ("Admin".equals(userType)) {
            for (Admin admin : admins) {
                if (admin.getLogin().authenticate(username, password)) {
                    JOptionPane.showMessageDialog(this, "Admin login successful! Welcome " + admin.getName(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.showAdminDashboard();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password.", 
            "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "User Registration", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField newUsernameField = new JTextField(20);
        JPasswordField newPasswordField = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(newUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String username = newUsernameField.getText().trim();
            String password = new String(newPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create new user
            User newUser = new User(users.size() + 1, name, email, phone);
            Login newLogin = new Login(username, password);
            newUser.setLogin(newLogin);
            users.add(newUser);

            JOptionPane.showMessageDialog(dialog, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        panel.add(registerBtn, gbc);
        dialog.add(panel);
        dialog.setVisible(true);
    }
}
