package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PunchDAO {
    private static final String QUERY_FIND = "SELECT terminalid, badgeid, timestamp, eventtypeid FROM event WHERE id = ?";
    private final DAOFactory daoFactory;
    private final BadgeDAO badgeDAO;

    // Constructor initializing the DAO with DAOFactory
    PunchDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.badgeDAO = daoFactory.getBadgeDAO();
    }

    public Punch find(Integer id) {
        Punch punch = null;

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_FIND)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int terminalId = rs.getInt("terminalid");
                String badgeID = rs.getString("badgeid");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                int eventTypeID = rs.getInt("eventtypeid");
                EventType punchType = null;
                 switch (eventTypeID) {
                    case 1:
                        punchType = EventType.CLOCK_IN;
                        break;
                    case 0:
                        punchType = EventType.CLOCK_OUT;
                        break;
                    case 2:
                        punchType = EventType.TIME_OUT;
                        break;
                    default:
                        // Handle the case where the eventtypeid is not recognized
                        System.err.println("Unknown eventtypeid: " + eventTypeID);
                        return null; // Or throw an exception
                }


                Badge badge = badgeDAO.find(badgeID);

                punch = new Punch(id, terminalId, badge, timestamp, punchType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return punch;
    }
    
    public int create(Punch punch) {
    int result = 0;

    try (Connection conn = daoFactory.getConnection()) {
        String query = "INSERT INTO event (terminalid, badgeid, timestamp, eventtypeid) VALUES (?, ?, ?, ?)";

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
                SELECT id, terminalid, badgeid, timestamp, eventtypeid FROM event
                WHERE badgeid = ? AND DATE(timestamp) = ?
                ORDER BY timestamp
                """;

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, badge.getId());
            ps.setDate(2, java.sql.Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int terminalId = rs.getInt("terminalid");
                    LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                     int eventTypeID = rs.getInt("eventtypeid"); // Get eventtypeid from the database

                    EventType punchType = null;
                    switch (eventTypeID) {
                        case 1:
                            punchType = EventType.CLOCK_IN;
                            break;
                        case 0:
                            punchType = EventType.CLOCK_OUT;
                            break;
                        case 2:
                            punchType = EventType.TIME_OUT;
                            break;
                        default:
                            // Handle the case where the eventtypeid is not recognized
                            System.err.println("Unknown eventtypeid: " + eventTypeID);
                            continue; // Skip this punch
                    }

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