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

public class UserDashboardPanel extends JPanel {
    private VehicleRentalApp parent;
    private JTabbedPane tabbedPane;
    private JTable vehicleTable;
    private JTable bookingTable;
    private JTable historyTable;
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel bookingTableModel;
    private DefaultTableModel historyTableModel;
    
    // Sample data
    private List<Vehicle> vehicles;
    private List<Booking> userBookings;
    private List<RentalHistory> rentalHistory;

    public UserDashboardPanel(VehicleRentalApp parent) {
        this.parent = parent;
        initializeSampleData();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeSampleData() {
        vehicles = new ArrayList<>();
        userBookings = new ArrayList<>();
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
        
        // Create sample rental history
        rentalHistory.add(new RentalHistory(1, 1, 1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(8), "COMPLETED"));
        rentalHistory.add(new RentalHistory(2, 1, 2, LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), "COMPLETED"));
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Vehicle Rental System");
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

        // Vehicle browsing tab
        createVehicleTab();
        
        // My bookings tab
        createBookingsTab();
        
        // Rental history tab
        createHistoryTab();

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createVehicleTab() {
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        vehiclePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Vehicle table
        String[] columnNames = {"ID", "Model", "Category", "Fuel Type", "Price/Hour", "Available"};
        vehicleTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
                vehicle.isAvailabilityStatus() ? "Yes" : "No"
            };
            vehicleTableModel.addRow(row);
        }

        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);
        vehicleScrollPane.setPreferredSize(new Dimension(800, 300));

        // Booking form
        JPanel bookingFormPanel = new JPanel(new GridBagLayout());
        bookingFormPanel.setBorder(BorderFactory.createTitledBorder("Make a Booking"));
        bookingFormPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel startTimeLabel = new JLabel("Start Time:");
        JTextField startTimeField = new JTextField(15);
        startTimeField.setText("09:00");

        JLabel endTimeLabel = new JLabel("End Time:");
        JTextField endTimeField = new JTextField(15);
        endTimeField.setText("17:00");

        JButton bookButton = new JButton("Book Selected Vehicle");
        bookButton.setBackground(new Color(60, 179, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        bookingFormPanel.add(startTimeLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        bookingFormPanel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        bookingFormPanel.add(endTimeLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        bookingFormPanel.add(endTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        bookingFormPanel.add(bookButton, gbc);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(vehiclePanel, "Please select a vehicle to book.", 
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Vehicle selectedVehicle = vehicles.get(selectedRow);
                if (!selectedVehicle.isAvailabilityStatus()) {
                    JOptionPane.showMessageDialog(vehiclePanel, "Selected vehicle is not available.", 
                        "Not Available", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String startTime = startTimeField.getText().trim();
                String endTime = endTimeField.getText().trim();

                if (startTime.isEmpty() || endTime.isEmpty()) {
                    JOptionPane.showMessageDialog(vehiclePanel, "Please enter both start and end times.", 
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create booking
                Booking booking = new Booking(userBookings.size() + 1, selectedVehicle, startTime, endTime);
                booking.reserveVehicle();
                userBookings.add(booking);

                // Update vehicle availability in table
                vehicleTableModel.setValueAt("No", selectedRow, 5);
                selectedVehicle.updateAvailability(false);

                // Switch to bookings tab
                tabbedPane.setSelectedIndex(1);
                refreshBookingsTable();

                JOptionPane.showMessageDialog(vehiclePanel, 
                    "Booking successful! Vehicle " + selectedVehicle.getModelName() + " booked from " + startTime + " to " + endTime, 
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        vehiclePanel.add(vehicleScrollPane, BorderLayout.CENTER);
        vehiclePanel.add(bookingFormPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Browse Vehicles", vehiclePanel);
    }

    private void createBookingsTab() {
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Booking ID", "Vehicle Model", "Start Time", "End Time", "Status", "Actions"};
        bookingTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setRowHeight(25);

        // Add action buttons to table
        bookingTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        bookingTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane bookingsScrollPane = new JScrollPane(bookingTable);
        bookingsScrollPane.setPreferredSize(new Dimension(800, 400));

        bookingsPanel.add(bookingsScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("My Bookings", bookingsPanel);
    }

    private void createHistoryTab() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Booking ID", "Vehicle ID", "Start Date", "End Date", "Status"};
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
                history.getVehicleID(),
                history.getStartDate(),
                history.getEndDate(),
                history.getStatus()
            };
            historyTableModel.addRow(row);
        }

        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setPreferredSize(new Dimension(800, 400));

        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Rental History", historyPanel);
    }

    private void setupLayout() {
        // Layout is already set up in initializeComponents
    }

    private void setupEventHandlers() {
        // Event handlers are set up in individual tab creation methods
    }

    private void refreshBookingsTable() {
        bookingTableModel.setRowCount(0);
        for (Booking booking : userBookings) {
            Object[] row = {
                booking.getBookingID(),
                booking.getVehicle().getModelName(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                "Cancel"
            };
            bookingTableModel.addRow(row);
        }
    }

    // Button renderer and editor for actions column
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

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
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
            if (isPushed) {
                // Handle cancel booking
                if (row < userBookings.size()) {
                    Booking booking = userBookings.get(row);
                    if ("CONFIRMED".equals(booking.getStatus()) || "PENDING".equals(booking.getStatus())) {
                        booking.cancelBooking();
                        
                        // Update vehicle availability
                        for (int i = 0; i < vehicles.size(); i++) {
                            if (vehicles.get(i).getVehicleID() == booking.getVehicle().getVehicleID()) {
                                vehicleTableModel.setValueAt("Yes", i, 5);
                                break;
                            }
                        }
                        
                        refreshBookingsTable();
                        JOptionPane.showMessageDialog(null, "Booking cancelled successfully.", 
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
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
}
