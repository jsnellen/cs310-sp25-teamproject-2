/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Department;

import java.sql.*;

import java.sql.SQLException;

/**
 *
 * @author Dillon
 */
public class DepartmentDAO {
    
    private final DAOFactory daoFactory;
    
    private static final String QUERY_FIND = "SELECT * FROM department WHERE id = ?";
    //Constructor initializing DAOFactory
    DepartmentDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    // Method to find department by ID
    /**
     * A find method to find Department by ID
     * @param id the ID of the department
     * @return department
     */
    public Department find(int id){
        Department department = null;
        //Initialize JDBC objects
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Obtain database connection from DAOFactory
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);
                //Execute the query
                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();
                    //Iterate through the result set
                    while (rs.next()) {
                        String description = rs.getString("description");
                        int terminalid = rs.getInt("terminalid");
                        
                        //Create a new Department object
                        department = new Department(id, description, terminalid);
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
        return department;//Return the department object;
    }
    
}
