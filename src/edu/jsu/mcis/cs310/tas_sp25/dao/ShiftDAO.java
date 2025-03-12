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

    private static final String QUERY_FIND = "SELECT * FROM shift WHERE id = ?";
    private static final String QUERY_FIND_BY_BADGE = "SELECT * FROM shift WHERE id = (SELECT shiftid FROM badge WHERE id = ?)";

    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Shift find(int id) {
        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasResults = ps.execute();

                if (hasResults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        // Create a HashMap to store the shift data
                        HashMap<String, String> shiftData = new HashMap<>();
                        shiftData.put("start", rs.getString("shiftstart")); // Corrected key
                        shiftData.put("stop", rs.getString("shiftstop"));
                        shiftData.put("lunchstart", rs.getString("lunchstart"));
                        shiftData.put("lunchstop", rs.getString("lunchstop"));
                        
                        // Create a new Shift object using the shiftData HashMap
                        shift = new Shift(shiftData);
                    }
                }
            }

        } catch (SQLException e) {
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

        return shift;
    }

    public Shift find(Badge badge) {
        Shift shift = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                ps = conn.prepareStatement(QUERY_FIND_BY_BADGE);
                ps.setString(1, badge.getId());

                boolean hasResults = ps.execute();

                if (hasResults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        int shiftId = rs.getInt("shiftid");
                        shift = find(shiftId);
                    }
                }
            }

        } catch (SQLException e) {
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

        return shift;
    }
}

