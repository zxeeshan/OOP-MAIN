package model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardAnalytics {
    private List<RentalHistory> rentalHistory;

    public DashboardAnalytics(List<RentalHistory> rentalHistory) {
        this.rentalHistory = rentalHistory;
    }

    // Methods from class diagram
    public double calculateRevenue() {
        // Simple revenue calculation - in real app would be more complex
        return rentalHistory.stream()
                .filter(history -> "COMPLETED".equals(history.getStatus()))
                .mapToDouble(history -> 100.0) // Placeholder calculation
                .sum();
    }

    public List<String> getTopRentedVehicles() {
        return rentalHistory.stream()
                .collect(Collectors.groupingBy(
                    RentalHistory::getVehicleID,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> "Vehicle ID: " + entry.getKey() + " - Rentals: " + entry.getValue())
                .collect(Collectors.toList());
    }

    public Map<String, Long> rentalsPerPeriod() {
        return rentalHistory.stream()
                .collect(Collectors.groupingBy(
                    history -> history.getStartDate().getMonth().toString(),
                    Collectors.counting()
                ));
    }
}
