/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Badge;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ShiftDAO {
    
    // create objects
    private static final String QUERY_FIND_BY_ID = "SELECT * FROM shift WHERE id = ?";
    private static final String QUERY_FIND_BY_BADGE = "SELECT shiftid FROM employee WHERE badgeid = ?";

    private final DAOFactory daoFactory;

    ShiftDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    // find method
    /**
     * A find method for the Shift using the ID
     * @param id the ID of the shift
     * @return shift
     */
    public Shift find(int id) {

        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BY_ID);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        // stores information in to a hashmap
                        
                        HashMap<String, String> shiftInfo = new HashMap<>();
                        shiftInfo.put("id", rs.getString("id"));
                        shiftInfo.put("description", rs.getString("description"));
                        shiftInfo.put("shiftStart", rs.getString("shiftstart"));
                        shiftInfo.put("shiftStop", rs.getString("shiftstop"));
                        shiftInfo.put("roundInterval", rs.getString("roundinterval"));
                        shiftInfo.put("gracePeriod", rs.getString("graceperiod"));
                        shiftInfo.put("dockPenalty", rs.getString("dockpenalty"));
                        shiftInfo.put("lunchStart", rs.getString("lunchstart"));
                        shiftInfo.put("lunchStop", rs.getString("lunchstop"));
                        shiftInfo.put("lunchThreshold", rs.getString("lunchthreshold"));
                        shift = new Shift (shiftInfo);
                        
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
    /**
     * A find method for the shift using the Badge ID
     * @param badge the Badge of the Employee
     * @return shift
     */
    public Shift find(Badge badge) {

        Shift shift = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND_BY_BADGE);
                ps.setString(1, badge.getId());

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        
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
