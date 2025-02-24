/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;
import edu.jsu.mcis.cs310.tas_sp25.*;
import java.sql.*;
import java.time.LocalDateTime;


public class PunchDAO {
    private final Connection connection; //Database connection object
    private final DAOFactory daoFactory; //DAOFactory for BadgeDAO
    
    //Constructor initializing the DAO with an existing database
    public PunchDAO(Connection connection, DAOFactory daoFactory){
        this.connection = connection;
        this.daoFactory = daoFactory;
    }
    
    
    //Find a Punch by its ID
    public Punch find(int id){
        String query = "SELECT terminalid, badgeid, timestamp, punchtype FROM event WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, id); //Setting the ID
            ResultSet rs = stmt.executeQuery(); //Executing
            
            if (rs.next()){ //If there is an ID to match
                int terminalId = rs.getInt("terminalid");
                String badgeID = rs.getString("badgeid");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                EventType punchType = EventType.values()[rs.getInt("punchtype")];
                
                
                //Supposed to retrieve the Badge that goes with the PunchID
                BadgeDAO badgeDAO = new BadgeDAO(daoFactory);
                Badge badge = badgeDAO.find(badgeID);
                
                //Should return the Punch and get the data
                return new Punch(id, terminalId, badge, timestamp, punchType); 
            }
        } catch (SQLException e){
            e.printStackTrace(); //Should handle the SQL Exceptions
        }
        return null;
    }
}
