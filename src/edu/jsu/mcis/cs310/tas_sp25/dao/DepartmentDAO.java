/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentDAO provides methods to create, retrieve, update, and delete
 * Department records from the database.
 */
public class DepartmentDAO {

    // Reference to a DAOFactory that manages the database connection.
    private final DAOFactory daoFactory;
    
    // Constructor
    public DepartmentDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    /**
     * Inserts a new Department record into the database.
     * 
     * @param department The Department model object.
     * @return true if the insertion was successful; false otherwise.
     */
    public boolean create(Department department) {
        String query = "INSERT INTO department (id, description, terminalid) VALUES (?, ?, ?)";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, department.getId());
            ps.setString(2, department.getDescription());
            ps.setInt(3, department.getTerminalid());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in DepartmentDAO.create(): " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a Department by its id.
     * 
     * @param id The id of the department.
     * @return A Department object if found; null otherwise.
     */
    public Department findById(int id) {
        String query = "SELECT * FROM department WHERE id = ?";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String description = rs.getString("description");
                    int terminalid = rs.getInt("terminalid");
                    return new Department(id, description, terminalid);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in DepartmentDAO.findById(): " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Retrieves a list of all Department records.
     * 
     * @return A List of Department objects.
     */
    public List<Department> getAll() {
        String query = "SELECT * FROM department";
        List<Department> list = new ArrayList<>();
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int terminalid = rs.getInt("terminalid");
                list.add(new Department(id, description, terminalid));
            }
        } catch (SQLException e) {
            System.err.println("Error in DepartmentDAO.getAll(): " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Updates an existing Department record.
     * 
     * @param department The Department object with updated values.
     * @return true if the update was successful; false otherwise.
     */
    public boolean update(Department department) {
        String query = "UPDATE department SET description = ?, terminalid = ? WHERE id = ?";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, department.getDescription());
            ps.setInt(2, department.getTerminalid());
            ps.setInt(3, department.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in DepartmentDAO.update(): " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a Department record from the database.
     * 
     * @param id The id of the department to delete.
     * @return true if the deletion was successful; false otherwise.
     */
    public boolean delete(int id) {
        String query = "DELETE FROM department WHERE id = ?";
        try (Connection conn = daoFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in DepartmentDAO.delete(): " + e.getMessage());
            return false;
        }
    }
}
