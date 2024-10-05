package entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdoptionEventManager {
    private Connection connection;

    public AdoptionEventManager(Connection connection) {
        this.connection = connection;
    }

    // Retrieve all upcoming adoption events
    public List<AdoptionEvent> getUpcomingEvents() {
        List<AdoptionEvent> events = new ArrayList<>();
        String query = "SELECT * FROM adoption_events WHERE event_date >= CURDATE()"; // Get events from today onwards
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AdoptionEvent event = new AdoptionEvent(
                        rs.getInt("id"),
                        rs.getString("event_name"),
                        rs.getDate("event_date").toLocalDate(),
                        rs.getString("location"),
                        rs.getString("description")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    // Register a participant for an event
    public void registerParticipant(Participant participant) {
        String query = "INSERT INTO participants (event_id, participant_name, participant_email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, participant.getEventId());
            pstmt.setString(2, participant.getParticipantName());
            pstmt.setString(3, participant.getParticipantEmail());
            pstmt.executeUpdate();
            System.out.println("Participant registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to register participant. Ensure that the event ID exists.");
        }
    }

}
