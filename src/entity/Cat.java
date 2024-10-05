package entity;

public class Cat extends Pet {
    private String color;

    // Constructor for Cat
    public Cat(String name, int age, String breed, String color) {
        super(name, age, breed, "Cat"); // Call the Pet constructor
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return super.toString() + ", Color: " + color;
    }
}
