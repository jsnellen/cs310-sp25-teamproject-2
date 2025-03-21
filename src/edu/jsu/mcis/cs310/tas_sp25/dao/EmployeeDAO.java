package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EmployeeDAO {

    private static final String QUERY_FIND = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND2 = "SELECT * FROM employee WHERE badgeid = ?";
    
    private final DAOFactory daoFactory;
    private final ShiftDAO shiftDAO;
    private final DepartmentDAO departmentDAO;
    private final BadgeDAO badgeDAO;
    
    EmployeeDAO(DAOFactory daoFactory, ShiftDAO shiftDAO, DepartmentDAO departmentDAO, BadgeDAO badgeDAO) {
        this.daoFactory = daoFactory;
        this.departmentDAO = departmentDAO;
        this.shiftDAO = shiftDAO;
        this.badgeDAO = badgeDAO;
    }

    public Employee find(Integer id) {
        Employee employee = null;

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_FIND)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Found Employee ID: " + rs.getInt("id"));

                    // Get Badge
                    String badgeId = rs.getString("badgeid");
                    Badge badge = badgeDAO.find(badgeId);
                    System.out.println("Badge ID: " + badgeId);

                    // Get Name
                    String[] fullName = badge.getDescription().split(",\\s+");
                    String lastName = fullName[0];
                    String firstName = fullName[1].split(" ")[0];
                    String middleName = (fullName[1].split(" ").length > 1) ? fullName[1].split(" ")[1] : "";

                    // Get Active Date
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime active = LocalDateTime.parse(rs.getString("active"), dtf);

                    // Get Department
                    int departmentId = rs.getInt("departmentid");
                    Department department = departmentDAO.find(departmentId);

                    // Get Shift
                    Shift shift = shiftDAO.find(rs.getInt("shiftid"));

                    // Get Employee Type
                    int employeeTypeNum = rs.getInt("employeetypeid");
                    EmployeeType employeeType = switch (employeeTypeNum) {
                        case 0 -> EmployeeType.PART_TIME;
                        case 1 -> EmployeeType.FULL_TIME;
                        default -> throw new IllegalArgumentException("Invalid employeeType id: " + employeeTypeNum);
                    };

                    // Create Employee Object
                    HashMap<String, Object> employeeParams = new HashMap<>();
                    employeeParams.put("id", id);
                    employeeParams.put("firstName", firstName);
                    employeeParams.put("middleName", middleName);
                    employeeParams.put("lastName", lastName);
                    employeeParams.put("active", active);
                    employeeParams.put("badge", badge);
                    employeeParams.put("department", department);
                    employeeParams.put("shift", shift);
                    employeeParams.put("employeeType", employeeType);

                    employee = new Employee(employeeParams);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("SQL Error in find(Integer id): " + e.getMessage());
        }

        return employee;
    }
    
    public Employee find(Badge badge) {
        Employee employee = null;

        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(QUERY_FIND2)) {

            ps.setString(1, badge.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int employeeId = rs.getInt("id");
                    System.out.println("Found Employee ID: " + employeeId + " for Badge: " + badge.getId());
                    employee = find(employeeId);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("SQL Error in find(Badge badge): " + e.getMessage());
        }

        return employee;
    }
}
