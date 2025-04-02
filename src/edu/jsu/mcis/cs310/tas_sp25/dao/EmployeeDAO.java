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
                // Extract data before calling other DAO methods
                String badgeId = rs.getString("badgeid");
                int departmentId = rs.getInt("departmentid");
                int shiftId = rs.getInt("shiftid");
                int employeeTypeNum = rs.getInt("employeetypeid");
                String activeString = rs.getString("active");

                // Convert DateTime
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime active = LocalDateTime.parse(activeString, dtf);

                // Fetch related objects AFTER result set is closed
                Badge badge = badgeDAO.find(badgeId);
                Department department = departmentDAO.find(departmentId);
                Shift shift = shiftDAO.find(shiftId);
                EmployeeType employeeType = (employeeTypeNum == 0) ? EmployeeType.PART_TIME : EmployeeType.FULL_TIME;

                // Extract employee name
                String[] fullName = badge.getDescription().split(",\\s+");
                String lastName = fullName[0];
                String firstName = fullName[1].split(" ")[0];
                String middleName = (fullName[1].split(" ").length > 1) ? fullName[1].split(" ")[1] : "";

                // Create Employee object
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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND2);
                ps.setString(1, badge.getId());
                rs = ps.executeQuery();

                if (rs.next()) {
                    int employeeId = rs.getInt("id");
                    employee = find(employeeId);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("SQL Error in find(Badge badge): " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }

        return employee;
    }
}
