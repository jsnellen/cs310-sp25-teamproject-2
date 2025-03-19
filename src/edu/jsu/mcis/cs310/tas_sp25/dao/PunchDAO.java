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
    private final BadgeDAO badgeDAO;

    // Constructor initializing the DAO with an existing database
    public PunchDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.connection = daoFactory.getConnection();
        this.badgeDAO = daoFactory.getBadgeDAO();
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
                Badge badge = badgeDAO.find(badgeID);

                // Return the Punch with the retrieved data
                return new Punch(id, terminalId, badge, timestamp, punchType);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return null;
    }
    
    public int create(Punch punch) {
    int result = 0;

    try (Connection conn = daoFactory.getConnection()) {
        String query = "INSERT INTO event (terminalid, badgeid, timestamp, punchtype) VALUES (?, ?, ?, ?)";

        // Retrieve employee (if applicable)
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        Employee employee = employeeDAO.find(punch.getBadge());

        // Validate terminal ID if employee exists
        if (employee != null) {
            Department employeeDepartment = employee.getDepartment();
            if (punch.getTerminalid() != employeeDepartment.getTerminalid()) {
                return 0; // Terminal ID mismatch, reject punch
            }
        }

        // Create and execute statement
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, punch.getTerminalid());
            ps.setString(2, punch.getBadge().getId());
            ps.setTimestamp(3, Timestamp.valueOf(punch.getOriginaltimestamp()));
            ps.setInt(4, punch.getPunchtype().ordinal()); // Store as integer

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) { // Ensure ResultSet is inside the try block
                if (rs.next()) {
                    result = rs.getInt(1); // Retrieve generated ID
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return result;
}
    // Adding the retrieve a list of Punch objects for a specific Badge on a specific day
    public ArrayList<Punch> list(Badge badge, LocalDate date) {
        ArrayList<Punch> punches = new ArrayList<>();

        // Query to retrieve punches for the specified day, ordered by timestamp
        String query = """
                SELECT id, terminalid, badgeid, timestamp, punchtype FROM event
                WHERE badgeid = ? AND DATE(timestamp) = ?
                ORDER BY timestamp
                """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, badge.getId());
            ps.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int terminalId = rs.getInt("terminalid");
                    LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                    EventType punchType = EventType.values()[rs.getInt("punchtype")];

                    punches.add(new Punch(id, terminalId, badge, timestamp, punchType));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return punches;
    }

    public ArrayList<Punch> list(Badge badge, LocalDate begin, LocalDate end) {
        ArrayList<Punch> punches = new ArrayList<>();

        // Iterate through the date range
        for (LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1)) {
            // Reuse the existing list(Badge badge, LocalDate date) method
            punches.addAll(list(badge, date));
        }

        return punches;
    }
}