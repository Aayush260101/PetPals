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
import entity.AdoptionEventManager;
import exception.InvalidPetAgeException;
import util.DBConnUtil;
import util.DBPropertyUtil;

public class MainModule {
    private static PetShelter petShelter;
    private static AdoptionEventManager eventManager;
    private static DBConnUtil dbConnUtil = new DBConnUtil(); // Database connection utility

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            // Load the database connection from db.properties
            connection = dbConnUtil.getConnectionFromProperties("db.properties");
            
            if (connection != null) {
                petShelter = new PetShelter(connection);  // Pass connection to PetShelter
                eventManager = new AdoptionEventManager(connection);  // Pass connection to Event Manager
                System.out.println("Database connected successfully!");
            } else {
                System.out.println("Failed to establish database connection.");
                return; // Exit the program if connection fails
            }

            // Main loop
            while (true) {
                System.out.println("\nWelcome to PetPals!");
                System.out.println("1. Add Pet");
                System.out.println("2. List Available Pets");
                System.out.println("3. Remove Pet");
                System.out.println("4. Make a Donation");
                System.out.println("5. View All Donations");
                System.out.println("6. Manage Adoption Events");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        addPet(scanner);
                        break;
                    case 2:
                        listAvailablePets();
                        break;
                    case 3:
                        removePet(scanner);
                        break;
                    case 4:
                        makeDonation(scanner);
                        break;
                    case 5:
                        viewAllDonations();
                        break;
                    case 6:
                        manageAdoptionEvents(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting the application.");
                        dbConnUtil.closeConnection(); // Close the connection
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
        } finally {
            dbConnUtil.closeConnection(); // Ensure the connection is closed
        }
    }

    // Add a new pet
    private static void addPet(Scanner scanner) {
        System.out.print("Enter pet type (dog/cat): ");
        String type = scanner.nextLine();
        System.out.print("Enter pet name: ");
        String name = scanner.nextLine();

        int age = 0;
        while (true) {
            System.out.print("Enter pet age: ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0) {
                    throw new InvalidPetAgeException("Pet age must be a positive integer.");
                }
                break;
            } catch (InvalidPetAgeException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer for age.");
            }
        }

        System.out.print("Enter pet breed: ");
        String breed = scanner.nextLine();

        Pet pet;
        if (type.equalsIgnoreCase("dog")) {
            System.out.print("Enter dog breed: ");
            String dogBreed = scanner.nextLine();
            pet = new Dog(name, age, breed, dogBreed);
        } else if (type.equalsIgnoreCase("cat")) {
            System.out.print("Enter cat color: ");
            String catColor = scanner.nextLine();
            pet = new Cat(name, age, breed, catColor);
        } else {
            System.out.println("Invalid pet type!");
            return;
        }

        petShelter.addPet(pet);  // Add the pet to the shelter
        System.out.println("Pet added successfully!");
    }

    // List all available pets
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

    // Remove a pet
    private static void removePet(Scanner scanner) {
        System.out.print("Enter the name of the pet to remove: ");
        String petName = scanner.nextLine();

        Pet petToRemove = petShelter.getAvailablePets().stream()
            .filter(pet -> pet.getName().equalsIgnoreCase(petName))
            .findFirst()
            .orElse(null);

        if (petToRemove != null) {
            petShelter.removePet(petToRemove);
            System.out.println(petName + " has been removed from the shelter.");
        } else {
            System.out.println("Pet not found!");
        }
    }

    // Make a donation
    private static void makeDonation(Scanner scanner) {
        System.out.print("Enter your name: ");
        String donorName = scanner.nextLine();

        double amount = 0.0;
        while (true) {
            System.out.print("Enter donation amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Donation amount must be greater than zero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }

        System.out.println("Select donation type:");
        System.out.println("1. Cash Donation");
        System.out.println("2. Item Donation");
        System.out.print("Enter your choice: ");
        int donationChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (donationChoice) {
            case 1:
                System.out.print("Enter donation date (YYYY-MM-DD): ");
                LocalDate donationDate = LocalDate.parse(scanner.nextLine());
                CashDonation cashDonation = new CashDonation(donorName, amount, donationDate);
                petShelter.addCashDonation(cashDonation);
                break;
            case 2:
                System.out.print("Enter item type (e.g., food, toys): ");
                String itemType = scanner.nextLine();
                ItemDonation itemDonation = new ItemDonation(donorName, amount, itemType);
                petShelter.addItemDonation(itemDonation);
                break;
            default:
                System.out.println("Invalid donation type!");
                break;
        }
    }

    // View all donations
    private static void viewAllDonations() {
        List<Donation> donations = petShelter.getAllDonations();
        if (donations.isEmpty()) {
            System.out.println("No donations available.");
        } else {
            System.out.println("List of Donations:");
            for (Donation donation : donations) {
                System.out.println(donation);
            }
        }
    }

    // Manage adoption events
    private static void manageAdoptionEvents(Scanner scanner) {
        System.out.println("1. View Upcoming Adoption Events");
        System.out.println("2. Register for an Event");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

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

    // View upcoming adoption events
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

    // Register for an event
    private static void registerForEvent(Scanner scanner) {
        System.out.print("Enter the event ID you want to register for: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter your name: ");
        String participantName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String participantEmail = scanner.nextLine();

        Participant participant = new Participant(eventId, participantName, participantEmail);
        eventManager.registerParticipant(participant);
        System.out.println("You have been registered for the event.");
    }
}
