package model;

public class PricingModel {
    private int pricingID;
    private String type;

    public PricingModel(int pricingID, String type) {
        this.pricingID = pricingID;
        this.type = type;
    }

    // Getters and Setters
    public int getPricingID() { return pricingID; }
    public void setPricingID(int pricingID) { this.pricingID = pricingID; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // Methods from class diagram
    public double getPrice(int hours, double basePrice) {
        switch (type.toLowerCase()) {
            case "hourly":
                return basePrice * hours;
            case "daily":
                return basePrice * Math.ceil(hours / 24.0);
            case "weekly":
                return basePrice * Math.ceil(hours / (24.0 * 7));
            default:
                return basePrice * hours;
        }
    }
}
