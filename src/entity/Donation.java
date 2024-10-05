package entity;

public abstract class Donation {
    private String donorName; // The name of the donor
    private double amount; // The donation amount

    // Constructor to initialize DonorName and Amount
    public Donation(String donorName, double amount) {
        this.donorName = donorName;
        this.amount = amount;
    }

    // Getters
    public String getDonorName() {
        return donorName;
    }

    public double getAmount() {
        return amount;
    }

    // Abstract method to record the donation
    public abstract void recordDonation();
    
    @Override
    public String toString() {
        return "Donor Name: " + donorName + ", Amount: " + amount;
    }
}
