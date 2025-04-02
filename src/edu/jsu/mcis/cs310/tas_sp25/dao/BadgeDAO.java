package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Badge;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BadgeDAO {

    private final Connection connection;
    
    // Assume the DAOFactory passes the connection to this DAO.
    public BadgeDAO(DAOFactory daoFactory) {
        this.connection = daoFactory.getConnection();
    }
    
    // Existing methods (such as find) are assumed to be present.
    
    /**
     * Creates a new badge record in the database.
     * @param badge The Badge object to insert.
     * @return true if exactly one row was inserted, false otherwise.
     */
    public boolean create(Badge badge) {
        String query = "INSERT INTO badge (id, description) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, badge.getId());
            ps.setString(2, badge.getDescription());
            int rowsAffected = ps.executeUpdate();
            return (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing badge record in the database.
     * The badge id remains unchanged.
     * @param badge The Badge object containing the updated description.
     * @return true if exactly one row was updated, false otherwise.
     */
    public boolean update(Badge badge) {
        String query = "UPDATE badge SET description = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, badge.getDescription());
            ps.setString(2, badge.getId());
            int rowsAffected = ps.executeUpdate();
            return (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes the badge record with the specified id.
     * @param badgeId The id of the badge to delete.
     * @return true if exactly one row was deleted, false otherwise.
     */
    public boolean delete(String badgeId) {
        String query = "DELETE FROM badge WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, badgeId);
            int rowsAffected = ps.executeUpdate();
            return (rowsAffected == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
