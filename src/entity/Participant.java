package entity;

public class Participant {
    private int id;
    private int eventId;
    private String participantName;
    private String participantEmail;

    // Constructor
    public Participant(int eventId, String participantName, String participantEmail) {
        this.eventId = eventId;
        this.participantName = participantName;
        this.participantEmail = participantEmail;
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getParticipantName() { return participantName; }
    public String getParticipantEmail() { return participantEmail; }
}
