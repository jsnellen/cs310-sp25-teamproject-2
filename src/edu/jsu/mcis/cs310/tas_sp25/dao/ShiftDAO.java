/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package edu.jsu.mcis.cs310.tas_sp25.dao;

/**
 *
 * @author brooklynleonard
 */
import edu.jsu.mcis.cs310.tas_sp25.*;

import java.sql.*;
import java.util.HashMap;

public class ShiftDAO {

    private Connection conn;

    // Constructor
    public ShiftDAO(Connection conn) {
        this.conn = conn;
    }

    // Finding shift by ID
    public Shift find(int id) {
        Shift shift = null;
        String query = "SELECT * FROM shift WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HashMap<String, String> shiftData = new HashMap<>();
                    shiftData.put("id", String.valueOf(rs.getInt("id")));
                    shiftData.put("start", rs.getString("start"));
                    shiftData.put("stop", rs.getString("stop"));
                    shiftData.put("lunchstart", rs.getString("lunchstart"));
                    shiftData.put("lunchstop", rs.getString("lunchstop"));
                    
                    // Create Shift object
                    shift = new Shift(shiftData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shift;
    }

    // Finding the shift by employee Badge
    public Shift find(Badge badge) {
        Shift shift = null;
        String query = "SELECT shiftid FROM employee WHERE badgeid = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, badge.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int shiftId = rs.getInt("shiftid");
                    shift = find(shiftId); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shift;
    }
}
