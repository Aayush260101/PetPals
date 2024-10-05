package entity;

public class ItemDonation extends Donation {
    private String itemName; // The type of item donated

    // Constructor to initialize itemName
    public ItemDonation(String donorName, double amount, String itemName) {
        super(donorName, amount);
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public void recordDonation() {
        System.out.println("Recording item donation: " + toString() + ", Item Name: " + itemName);
        // Here, you would add logic to save the donation to the database
    }

    @Override
    public String toString() {
        return super.toString() + ", Item Name: " + itemName;
    }
}
