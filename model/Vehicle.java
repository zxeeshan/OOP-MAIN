package model;

public class Vehicle {
    private int vehicleID;
    private String modelName;
    private String category;
    private String fuelType;
    private boolean availabilityStatus;
    private double basePrice;
    private PricingModel pricingModel;

    public Vehicle(int vehicleID, String modelName, String category, String fuelType, double basePrice) {
        this.vehicleID = vehicleID;
        this.modelName = modelName;
        this.category = category;
        this.fuelType = fuelType;
        this.basePrice = basePrice;
        this.availabilityStatus = true;
    }

    // Getters and Setters
    public int getVehicleID() { return vehicleID; }
    public void setVehicleID(int vehicleID) { this.vehicleID = vehicleID; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public boolean isAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(boolean availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public PricingModel getPricingModel() { return pricingModel; }
    public void setPricingModel(PricingModel pricingModel) { this.pricingModel = pricingModel; }

    // Methods from class diagram
    public double calculatePrice(int hours) {
        if (pricingModel != null) {
            return pricingModel.getPrice(hours, basePrice);
        }
        return basePrice * hours;
    }

    public void updateAvailability(boolean status) {
        this.availabilityStatus = status;
    }
}
