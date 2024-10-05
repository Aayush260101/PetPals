package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

import entity.Cat;
import entity.Dog;
import entity.Pet;
import entity.PetShelter;
import entity.Donation;
import entity.CashDonation;
import entity.ItemDonation;
import entity.AdoptionEvent;
import entity.Participant;
import entity.AdoptionEventManager; // Import the new class
import exception.InvalidPetAgeException;
import util.DatabaseContext;

public class MainModule {
    private static PetShelter petShelter;  // To manage the shelter
    private static AdoptionEventManager eventManager; // To manage adoption events
    private static DatabaseContext dbContext = new DatabaseContext();  // To manage database connection

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = dbContext.getConnection(); // Obtain the connection
            petShelter = new PetShelter(connection); // Pass connection to PetShelter
            eventManager = new AdoptionEventManager(connection); // Initialize the event manager
            System.out.println("Database connected successfully!");

            while (true) {
                System.out.println("\nWelcome to PetPals!");
                System.out.println("1. Add Pet");
                System.out.println("2. List Available Pets");
                System.out.println("3. Remove Pet");
                System.out.println("4. Make a Donation");
                System.out.println("5. View All Donations");
                System.out.println("6. Manage Adoption Events"); // New option
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        addPet(scanner);
                        break;
                    case 2:
                        listAvailablePets(); // List pets
                        break;
                    case 3:
                        removePet(scanner);
                        break;
                    case 4:
                        makeDonation(scanner);
                        break;
                    case 5:
                        viewAllDonations(); // View all donations
                        break;
                    case 6:
                        manageAdoptionEvents(scanner); // Manage events
                        break;
                    case 7:
                        System.out.println("Exiting the application.");
                        dbContext.closeConnection(); // Close the connection
                        System.out.println("Database connection closed.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Method to add a pet
    private static void addPet(Scanner scanner) {
        System.out.print("Enter pet type (dog/cat): ");
        String type = scanner.next();
        System.out.print("Enter pet name: ");
        String name = scanner.next();
        
        int age = 0;
        while (true) {
            System.out.print("Enter pet age: ");
            try {
                age = scanner.nextInt();
                if (age <= 0) {
                    throw new InvalidPetAgeException("Pet age must be a positive integer.");
                }
                break;
            } catch (InvalidPetAgeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Please enter a valid integer for age.");
                scanner.next(); // Clear invalid input
            }
        }
        
        System.out.print("Enter pet breed: ");
        String breed = scanner.next();

        Pet pet;
        if (type.equalsIgnoreCase("dog")) {
            System.out.print("Enter dog breed: ");
            String dogBreed = scanner.next();
            pet = new Dog(name, age, breed, dogBreed);
        } else if (type.equalsIgnoreCase("cat")) {
            System.out.print("Enter cat color: ");
            String catColor = scanner.next();
            pet = new Cat(name, age, breed, catColor);
        } else {
            System.out.println("Invalid pet type!");
            return; // Exit the method if pet type is invalid
        }
        
        petShelter.addPet(pet); // Add pet to the shelter
        System.out.println("Pet added successfully!");
    }

    // Method to list all available pets
    private static void listAvailablePets() {
        List<Pet> pets = petShelter.getAvailablePets();
        if (pets.isEmpty()) {
            System.out.println("No pets available.");
        } else {
            System.out.println("Available Pets:");
            for (Pet pet : pets) {
                System.out.println(pet);
            }
        }
    }

    // Method to remove a pet
    private static void removePet(Scanner scanner) {
        System.out.print("Enter the name of the pet to remove: ");
        String petName = scanner.next();
        
        Pet petToRemove = null;
        for (Pet pet : petShelter.getAvailablePets()) { // Use getAvailablePets() method
            if (pet.getName().equalsIgnoreCase(petName)) {
                petToRemove = pet;
                break;
            }
        }
        
        if (petToRemove != null) {
            petShelter.removePet(petToRemove);
            System.out.println(petName + " has been removed from the shelter.");
        } else {
            System.out.println("Pet not found!");
        }
    }

    // Method to make a donation
    private static void makeDonation(Scanner scanner) {
        System.out.print("Enter your name: ");
        String donorName = scanner.next();
        double amount = 0.0;
        boolean validAmount = false;

        // Loop until a valid amount is entered
        while (!validAmount) {
            System.out.print("Enter donation amount: ");
            try {
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Donation amount must be greater than zero. Please try again.");
                } else {
                    validAmount = true; // Valid amount entered
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number for the donation amount.");
                scanner.next(); // Clear the invalid input
            }
        }
        
        System.out.println("Select donation type:");
        System.out.println("1. Cash Donation");
        System.out.println("2. Item Donation");
        System.out.print("Enter your choice: ");
        
        int donationChoice = scanner.nextInt();

        switch (donationChoice) {
            case 1:
                System.out.print("Enter donation date (YYYY-MM-DD): ");
                String dateInput = scanner.next();
                LocalDate donationDate = LocalDate.parse(dateInput); // Parse the date input
                CashDonation cashDonation = new CashDonation(donorName, amount, donationDate);
                petShelter.addCashDonation(cashDonation); // Record the cash donation
                break;
            case 2:
                System.out.print("Enter item type (e.g., food, toys): ");
                String itemType = scanner.next();
                ItemDonation itemDonation = new ItemDonation(donorName, amount, itemType);
                petShelter.addItemDonation(itemDonation); // Record the item donation
                break;
            default:
                System.out.println("Invalid donation type!");
                break;
        }
    }

    // Method to view all donations
 // Method to view all donations
    private static void viewAllDonations() {
        List<Donation> donations = petShelter.getAllDonations(); // Get all donations
        if (donations.isEmpty()) {
            System.out.println("No donations available.");
        } else {
            System.out.println("List of Donations:");
            for (Donation donation : donations) {
                System.out.println(donation);
            }
        }
    }


    // New method to manage adoption events
    private static void manageAdoptionEvents(Scanner scanner) {
        System.out.println("1. View Upcoming Adoption Events");
        System.out.println("2. Register for an Event");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewUpcomingEvents();
                break;
            case 2:
                registerForEvent(scanner);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    // Method to view upcoming events
    private static void viewUpcomingEvents() {
        List<AdoptionEvent> events = eventManager.getUpcomingEvents();
        if (events.isEmpty()) {
            System.out.println("No upcoming adoption events.");
        } else {
            System.out.println("Upcoming Adoption Events:");
            for (AdoptionEvent event : events) {
                System.out.println(event);
            }
        }
    }

    // Method to register for an event
    private static void registerForEvent(Scanner scanner) {
        System.out.print("Enter the event ID you want to register for: ");
        int eventId = scanner.nextInt();
        System.out.print("Enter your name: ");
        String participantName = scanner.next();
        System.out.print("Enter your email: ");
        String participantEmail = scanner.next();

        Participant participant = new Participant(eventId, participantName, participantEmail);
        eventManager.registerParticipant(participant); // Register the participant
    }
}
