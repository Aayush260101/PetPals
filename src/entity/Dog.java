package entity;

public class Dog extends Pet {
    private String dogBreed;

    // Constructor for Dog
    public Dog(String name, int age, String breed, String dogBreed) {
        super(name, age, breed, "Dog"); // Call the Pet constructor
        this.dogBreed = dogBreed;
    }

    public String getDogBreed() {
        return dogBreed;
    }

    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }

    @Override
    public String toString() {
        return super.toString() + ", Dog Breed: " + dogBreed;
    }
}
