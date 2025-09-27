package frontend;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import app.VehicleRentalApp;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardPanel extends JPanel {
    private VehicleRentalApp parent;
    private JTabbedPane tabbedPane;
    private JTable vehicleTable;
    private JTable userTable;
    private JTable bookingTable;
    private JTable historyTable;
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel userTableModel;
    private DefaultTableModel bookingTableModel;
    private DefaultTableModel historyTableModel;
    
    // Sample data
    private List<Vehicle> vehicles;
    private List<User> users;
    private List<Booking> allBookings;
    private List<RentalHistory> rentalHistory;
    private DashboardAnalytics analytics;

    public AdminDashboardPanel(VehicleRentalApp parent) {
        this.parent = parent;
        initializeSampleData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeSampleData() {
        vehicles = new ArrayList<>();
        users = new ArrayList<>();
        allBookings = new ArrayList<>();
        rentalHistory = new ArrayList<>();
        
        // Create sample vehicles
        vehicles.add(new Vehicle(1, "Toyota Camry", "Sedan", "Gasoline", 25.0));
        vehicles.add(new Vehicle(2, "Honda Civic", "Sedan", "Gasoline", 22.0));
        vehicles.add(new Vehicle(3, "Tesla Model 3", "Sedan", "Electric", 35.0));
        vehicles.add(new Vehicle(4, "Ford F-150", "Truck", "Gasoline", 40.0));
        vehicles.add(new Vehicle(5, "BMW X5", "SUV", "Gasoline", 50.0));
        
        // Set pricing models
        for (Vehicle vehicle : vehicles) {
            vehicle.setPricingModel(new PricingModel(1, "hourly"));
        }
        
        // Create sample users
        users.add(new User(1, "John Doe", "john@email.com", "123-456-7890"));
        users.add(new User(2, "Jane Smith", "jane@email.com", "098-765-4321"));
        users.add(new User(3, "Bob Johnson", "bob@email.com", "555-123-4567"));
        
        // Create sample rental history
        rentalHistory.add(new RentalHistory(1, 1, 1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(8), "COMPLETED"));
        rentalHistory.add(new RentalHistory(2, 1, 2, LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), "COMPLETED"));
        rentalHistory.add(new RentalHistory(3, 2, 3, LocalDate.now().minusDays(7), LocalDate.now().minusDays(5), "COMPLETED"));
        
        analytics = new DashboardAnalytics(rentalHistory);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Admin Dashboard - Vehicle Rental System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> parent.showLogin());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Analytics dashboard tab
        createAnalyticsTab();
        
        // Vehicle management tab
        createVehicleManagementTab();
        
        // User management tab
        createUserManagementTab();
        
        // Booking management tab
        createBookingManagementTab();
        
        // Rental history tab
        createHistoryTab();

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createAnalyticsTab() {
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Analytics cards
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        cardsPanel.setBackground(new Color(240, 248, 255));

        // Revenue card
        JPanel revenueCard = createAnalyticsCard("Total Revenue", "$" + String.format("%.2f", analytics.calculateRevenue()), 
            new Color(60, 179, 113));
        cardsPanel.add(revenueCard);

        // Total rentals card
        JPanel rentalsCard = createAnalyticsCard("Total Rentals", String.valueOf(rentalHistory.size()), 
            new Color(70, 130, 180));
        cardsPanel.add(rentalsCard);

        // Available vehicles card
        long availableVehicles = vehicles.stream().mapToLong(v -> v.isAvailabilityStatus() ? 1 : 0).sum();
        JPanel availableCard = createAnalyticsCard("Available Vehicles", String.valueOf(availableVehicles), 
            new Color(255, 140, 0));
        cardsPanel.add(availableCard);

        // Total users card
        JPanel usersCard = createAnalyticsCard("Total Users", String.valueOf(users.size()), 
            new Color(220, 20, 60));
        cardsPanel.add(usersCard);

        // Top rented vehicles
        JPanel topVehiclesPanel = new JPanel(new BorderLayout());
        topVehiclesPanel.setBorder(BorderFactory.createTitledBorder("Top Rented Vehicles"));
        topVehiclesPanel.setBackground(Color.WHITE);

        List<String> topVehicles = analytics.getTopRentedVehicles();
        JList<String> topVehiclesList = new JList<>(topVehicles.toArray(new String[0]));
        topVehiclesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane topVehiclesScroll = new JScrollPane(topVehiclesList);
        topVehiclesPanel.add(topVehiclesScroll, BorderLayout.CENTER);

        // Monthly rentals chart (simplified)
        JPanel monthlyPanel = new JPanel(new BorderLayout());
        monthlyPanel.setBorder(BorderFactory.createTitledBorder("Rentals by Month"));
        monthlyPanel.setBackground(Color.WHITE);

        var monthlyData = analytics.rentalsPerPeriod();
        JList<String> monthlyList = new JList<>(monthlyData.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue() + " rentals")
            .toArray(String[]::new));
        JScrollPane monthlyScroll = new JScrollPane(monthlyList);
        monthlyPanel.add(monthlyScroll, BorderLayout.CENTER);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartsPanel.add(topVehiclesPanel);
        chartsPanel.add(monthlyPanel);

        analyticsPanel.add(cardsPanel, BorderLayout.NORTH);
        analyticsPanel.add(chartsPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Analytics Dashboard", analyticsPanel);
    }

    private JPanel createAnalyticsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void createVehicleManagementTab() {
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        vehiclePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Vehicle table
        String[] columnNames = {"ID", "Model", "Category", "Fuel Type", "Price/Hour", "Available", "Actions"};
        vehicleTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setRowHeight(25);

        // Populate vehicle table
        for (Vehicle vehicle : vehicles) {
            Object[] row = {
                vehicle.getVehicleID(),
                vehicle.getModelName(),
                vehicle.getCategory(),
                vehicle.getFuelType(),
                "$" + vehicle.getBasePrice(),
                vehicle.isAvailabilityStatus() ? "Yes" : "No",
                "Edit"
            };
            vehicleTableModel.addRow(row);
        }

        // Add action buttons to table
        vehicleTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        vehicleTable.getColumn("Actions").setCellEditor(new VehicleButtonEditor(new JCheckBox()));

        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);

        // Add vehicle form
        JPanel addVehiclePanel = new JPanel(new GridBagLayout());
        addVehiclePanel.setBorder(BorderFactory.createTitledBorder("Add New Vehicle"));
        addVehiclePanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField modelField = new JTextField(15);
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Sedan", "SUV", "Truck", "Hatchback"});
        JComboBox<String> fuelCombo = new JComboBox<>(new String[]{"Gasoline", "Electric", "Hybrid", "Diesel"});
        JTextField priceField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        addVehiclePanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        addVehiclePanel.add(modelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        addVehiclePanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        addVehiclePanel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        addVehiclePanel.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        addVehiclePanel.add(fuelCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        addVehiclePanel.add(new JLabel("Price/Hour:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        addVehiclePanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = new JButton("Add Vehicle");
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> {
            try {
                String model = modelField.getText().trim();
                String category = (String) categoryCombo.getSelectedItem();
                String fuelType = (String) fuelCombo.getSelectedItem();
                double price = Double.parseDouble(priceField.getText().trim());

                if (model.isEmpty()) {
                    JOptionPane.showMessageDialog(vehiclePanel, "Please enter a model name.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Vehicle newVehicle = new Vehicle(vehicles.size() + 1, model, category, fuelType, price);
                newVehicle.setPricingModel(new PricingModel(1, "hourly"));
                vehicles.add(newVehicle);

                // Add to table
                Object[] row = {
                    newVehicle.getVehicleID(),
                    newVehicle.getModelName(),
                    newVehicle.getCategory(),
                    newVehicle.getFuelType(),
                    "$" + newVehicle.getBasePrice(),
                    newVehicle.isAvailabilityStatus() ? "Yes" : "No",
                    "Edit"
                };
                vehicleTableModel.addRow(row);

                // Clear form
                modelField.setText("");
                priceField.setText("");

                JOptionPane.showMessageDialog(vehiclePanel, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vehiclePanel, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addVehiclePanel.add(addButton, gbc);

        vehiclePanel.add(vehicleScrollPane, BorderLayout.CENTER);
        vehiclePanel.add(addVehiclePanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Vehicle Management", vehiclePanel);
    }

    private void createUserManagementTab() {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"ID", "Name", "Email", "Phone", "Actions"};
        userTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only actions column is editable
            }
        };
        userTable = new JTable(userTableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);

        // Populate user table
        for (User user : users) {
            Object[] row = {
                user.getUserID(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                "View Details"
            };
            userTableModel.addRow(row);
        }

        // Add action buttons to table
        userTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Actions").setCellEditor(new UserButtonEditor(new JCheckBox()));

        JScrollPane userScrollPane = new JScrollPane(userTable);
        userPanel.add(userScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("User Management", userPanel);
    }

    private void createBookingManagementTab() {
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Booking ID", "User", "Vehicle", "Start Time", "End Time", "Status", "Actions"};
        bookingTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setRowHeight(25);

        // Add action buttons to table
        bookingTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        bookingTable.getColumn("Actions").setCellEditor(new BookingButtonEditor(new JCheckBox()));

        JScrollPane bookingScrollPane = new JScrollPane(bookingTable);
        bookingPanel.add(bookingScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Booking Management", bookingPanel);
    }

    private void createHistoryTab() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Booking ID", "User ID", "Vehicle ID", "Start Date", "End Date", "Status"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(historyTableModel);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.setRowHeight(25);

        // Populate history table
        for (RentalHistory history : rentalHistory) {
            Object[] row = {
                history.getBookingID(),
                history.getUserID(),
                history.getVehicleID(),
                history.getStartDate(),
                history.getEndDate(),
                history.getStatus()
            };
            historyTableModel.addRow(row);
        }

        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Rental History", historyPanel);
    }

    private void setupLayout() {
        // Layout is already set up in initializeComponents
    }

    private void setupEventHandlers() {
        // Event handlers are set up in individual tab creation methods
    }

    // Button renderer and editors for actions column
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class VehicleButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public VehicleButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed && row < vehicles.size()) {
                Vehicle vehicle = vehicles.get(row);
                showVehicleEditDialog(vehicle, row);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    class UserButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public UserButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed && row < users.size()) {
                User user = users.get(row);
                showUserDetailsDialog(user);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    class BookingButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public BookingButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed && row < allBookings.size()) {
                Booking booking = allBookings.get(row);
                showBookingDetailsDialog(booking);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private void showVehicleEditDialog(Vehicle vehicle, int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Vehicle", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField modelField = new JTextField(vehicle.getModelName(), 20);
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Sedan", "SUV", "Truck", "Hatchback"});
        categoryCombo.setSelectedItem(vehicle.getCategory());
        JComboBox<String> fuelCombo = new JComboBox<>(new String[]{"Gasoline", "Electric", "Hybrid", "Diesel"});
        fuelCombo.setSelectedItem(vehicle.getFuelType());
        JTextField priceField = new JTextField(String.valueOf(vehicle.getBasePrice()), 20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(modelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Fuel Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fuelCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Price/Hour:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            try {
                vehicle.setModelName(modelField.getText().trim());
                vehicle.setCategory((String) categoryCombo.getSelectedItem());
                vehicle.setFuelType((String) fuelCombo.getSelectedItem());
                vehicle.setBasePrice(Double.parseDouble(priceField.getText().trim()));

                // Update table
                vehicleTableModel.setValueAt(vehicle.getModelName(), row, 1);
                vehicleTableModel.setValueAt(vehicle.getCategory(), row, 2);
                vehicleTableModel.setValueAt(vehicle.getFuelType(), row, 3);
                vehicleTableModel.setValueAt("$" + vehicle.getBasePrice(), row, 4);

                JOptionPane.showMessageDialog(dialog, "Vehicle updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveButton, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showUserDetailsDialog(User user) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "User Details", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(String.valueOf(user.getUserID())), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(user.getName()), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(user.getEmail()), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(user.getPhone()), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showBookingDetailsDialog(Booking booking) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Booking Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Booking ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(String.valueOf(booking.getBookingID())), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Vehicle:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(booking.getVehicle().getModelName()), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(booking.getStartTime()), gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(booking.getEndTime()), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(booking.getStatus()), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(closeButton, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
