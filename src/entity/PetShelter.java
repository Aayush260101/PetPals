package entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class PetShelter {
    private Connection connection;

    public PetShelter(Connection connection) {
        this.connection = connection;
    }

    // Add a pet to the database
    public void addPet(Pet pet) {
        String query = "INSERT INTO pets (name, age, breed, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pet.getName());
            pstmt.setInt(2, pet.getAge());
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getType());
            pstmt.executeUpdate();
            System.out.println("Pet added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List all available pets
    public List<Pet> getAvailablePets() {
        List<Pet> pets = new ArrayList<>();
        String query = "SELECT * FROM pets";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Pet pet = new Pet(rs.getString("name"), rs.getInt("age"), rs.getString("breed"), rs.getString("type"));
                pet.setId(rs.getInt("id")); // Set the id
                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    // Remove a pet from the database
    public void removePet(Pet pet) {
        String query = "DELETE FROM pets WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pet.getName());
            pstmt.executeUpdate();
            System.out.println("Pet removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Add a cash donation to the database
    public void addCashDonation(CashDonation donation) {
        String query = "INSERT INTO donation (donor_name, amount, donation_type, donation_date) VALUES (?, ?, 'Cash', ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, donation.getDonorName());
            pstmt.setDouble(2, donation.getAmount());
            pstmt.setDate(3, Date.valueOf(donation.getDonationDate())); // Convert LocalDate to java.sql.Date
            pstmt.executeUpdate();
            System.out.println("Cash donation recorded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Add an item donation to the database
    public void addItemDonation(ItemDonation donation) {
        String query = "INSERT INTO donation (donor_name, amount, donation_type, donation_date, item_name) VALUES (?, ?, 'Item', ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, donation.getDonorName());
            pstmt.setDouble(2, donation.getAmount());
            pstmt.setDate(3, Date.valueOf(LocalDate.now())); // Use today's date for item donations
            pstmt.setString(4, donation.getItemName()); // Save item name
            pstmt.executeUpdate();
            System.out.println("Item donation recorded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all donations
    public List<Donation> getAllDonations() {
        List<Donation> donations = new ArrayList<>();
        String query = "SELECT donor_name, amount, donation_type, donation_date, item_name FROM donation"; // Include item_name
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String type = rs.getString("donation_type");
                if ("Cash".equalsIgnoreCase(type)) {
                    donations.add(new CashDonation(rs.getString("donor_name"), rs.getDouble("amount"), rs.getDate("donation_date").toLocalDate()));
                } else if ("Item".equalsIgnoreCase(type)) {
                    donations.add(new ItemDonation(rs.getString("donor_name"), rs.getDouble("amount"), rs.getString("item_name"))); // Use the item_name column
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donations;
    }


}
