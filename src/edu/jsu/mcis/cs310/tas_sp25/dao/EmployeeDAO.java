/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Badge;
import edu.jsu.mcis.cs310.tas_sp25.Department;
import edu.jsu.mcis.cs310.tas_sp25.Employee;
import edu.jsu.mcis.cs310.tas_sp25.EmployeeType;
import edu.jsu.mcis.cs310.tas_sp25.Shift;

import java.sql.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author TIMI
 */
public class EmployeeDAO {
    // creates DAO Factory object
    private final DAOFactory daoFactory;
    
    private static final String QUERY_FIND = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BADGE = "SELECT * FROM employee WHERE badgeid = ?";

    EmployeeDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;
    }
    // method for finds parts in the database for Employee
    /**
     * A find method to find Employee by the ID
     * @param id the ID of Employee
     * @return Employee
     */
    public Employee find(int id){
        
        Employee employee = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();
                      // gives information to the Employee
                    while (rs.next()) {
                        BadgeDAO badgedao = new BadgeDAO(daoFactory);
                        ShiftDAO shiftdao = new ShiftDAO(daoFactory);
                        DepartmentDAO departmentdao = new DepartmentDAO(daoFactory);
                        String firstname = rs.getString("firstname");
                        String lastname = rs.getString("lastname");
                        String middlename = rs.getString("middlename");
                        LocalDateTime active = rs.getTimestamp("active").toLocalDateTime();
                        EmployeeType employeeType = EmployeeType.values()[rs.getInt("employeeTypeID")];
                        Badge badge = badgedao.find(rs.getString("badgeid"));
                        Department department = departmentdao.find(rs.getInt("departmentid"));
                        Shift shift = shiftdao.find(badge);
                        
                        employee = new Employee(id,firstname,middlename,lastname,active,badge,department,shift,employeeType);

                    }

                }

            }

        }

       
        catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }
        
         return employee;
}
     // find method
    /**
     * A find method using Badge ID to find Employee
     * @param badge the badge of the Employee
     * @return Employee
     */
    public Employee find(Badge badge){
        
        Employee employee = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BADGE);
                ps.setString(1, badge.getId());
                  

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        ShiftDAO shiftdao = new ShiftDAO(daoFactory);
                        DepartmentDAO departmentdao = new DepartmentDAO(daoFactory);
                        int id = rs.getInt("id");
                        String firstname = rs.getString("firstname");
                        String lastname = rs.getString("lastname");
                        String middlename = rs.getString("middlename");
                        LocalDateTime active = rs.getTimestamp("active").toLocalDateTime();
                        EmployeeType employeeType = EmployeeType.values()[rs.getInt("employeeTypeID")];
                        Department department = departmentdao.find(rs.getInt("departmentid"));
                        Shift shift = shiftdao.find(rs.getInt("shiftid"));
                        employee = new Employee(id,firstname,middlename,lastname,active,badge,department,shift,employeeType);

                    }
                        
                }

            }

        }

        
         // throw and exception messages
        catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }
        
         return employee;
         
    }
    
}