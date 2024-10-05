package entity;

import java.time.LocalDate;

public class AdoptionEvent {
    private int id;
    private String eventName;
    private LocalDate eventDate;
    private String location;
    private String description;

    // Constructor
    public AdoptionEvent(int id, String eventName, LocalDate eventDate, String location, String description) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getEventName() { return eventName; }
    public LocalDate getEventDate() { return eventDate; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "AdoptionEvent{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
