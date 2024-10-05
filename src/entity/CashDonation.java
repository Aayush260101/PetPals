package entity;

import java.time.LocalDate; // Import LocalDate for donation date

public class CashDonation extends Donation {
    private LocalDate donationDate; // The date of the cash donation

    // Constructor to initialize DonationDate
    public CashDonation(String donorName, double amount, LocalDate donationDate) {
        super(donorName, amount);
        this.donationDate = donationDate;
    }

    // Getter for donationDate
    public LocalDate getDonationDate() {
        return donationDate;
    }

    @Override
    public void recordDonation() {
        System.out.println("Recording cash donation: " + toString() + ", Donation Date: " + donationDate);
        // Here, you would add logic to save the donation to the database
    }

    @Override
    public String toString() {
        return super.toString() + ", Donation Date: " + donationDate;
    }
}
