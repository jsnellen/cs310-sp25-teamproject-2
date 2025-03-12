/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PunchDAO {
    private final Connection connection; // Database connection object
    private final DAOFactory daoFactory; // DAOFactory for BadgeDAO

    // Constructor initializing the DAO with an existing database
    public PunchDAO(Connection connection, DAOFactory daoFactory) {
        this.connection = connection;
        this.daoFactory = daoFactory;
    }

    // Find a Punch by its ID
    public Punch find(int id) {
        String query = "SELECT terminalid, badgeid, timestamp, punchtype FROM event WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id); // Setting the ID
            ResultSet rs = stmt.executeQuery(); // Executing

            if (rs.next()) { // If there is an ID to match
                int terminalId = rs.getInt("terminalid");
                String badgeID = rs.getString("badgeid");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                EventType punchType = EventType.values()[rs.getInt("punchtype")];

                // Retrieve the Badge that corresponds to the Punch ID
                BadgeDAO badgeDAO = new BadgeDAO(daoFactory);
                Badge badge = badgeDAO.find(badgeID);

                // Return the Punch with the retrieved data
                return new Punch(id, terminalId, badge, timestamp, punchType);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return null;
    }

        // First List method that retrieves punches for a specified day )
    public ArrayList<Punch> list(Badge badge, LocalDate date) {
        ArrayList<Punch> punches = new ArrayList<>();
        String query = """
                SELECT id, terminalid, badgeid, timestamp, punchtype FROM event
                WHERE badgeid = ?
                AND timestamp >= ? AND timestamp < ?
                ORDER BY timestamp
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, badge.getId());
            ps.setString(2, date.atStartOfDay().toString()); // Start of the day
            ps.setString(3, date.plusDays(1).atStartOfDay().toString()); // Start of the next day

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Punch punch = createPunchFromResultSet(rs, badge);
                    punches.add(punch);
                }
            }

            // Post-process to check for late clock-out on the next day (if needed)
            // This part depends on your exact requirements.  You might be able to
            // handle this logic directly in the main query with a more complex WHERE clause.

        } catch (SQLException e) {
            e.printStackTrace(); // Removed logger, using basic stack trace
            throw new RuntimeException("Failed to retrieve punches", e); // Or a custom exception
            
        }

        return punches;
    }

    // New List method to retrieve punches within a date range
    public ArrayList<Punch> list(Badge badge, LocalDate begin, LocalDate end) {
        ArrayList<Punch> allPunches = new ArrayList<>();
        LocalDate currentDate = begin;

        while (!currentDate.isAfter(end)) {
            ArrayList<Punch> dailyPunches = list(badge, currentDate); // Reuse the existing method
            allPunches.addAll(dailyPunches); // Gathering all the punches
            currentDate = currentDate.plusDays(1); // Moving it to the next day
        }

        return allPunches;
    }

    private Punch createPunchFromResultSet(ResultSet rs, Badge badge) throws SQLException {
        int id = rs.getInt("id");
        int terminalId = rs.getInt("terminalid");
        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
        EventType punchType = EventType.values()[rs.getInt("punchtype")];

        return new Punch(id, terminalId, badge, timestamp, punchType);
    }

    
    
}
