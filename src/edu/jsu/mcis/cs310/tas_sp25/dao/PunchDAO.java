package edu.jsu.mcis.cs310.tas_sp25.dao;

import edu.jsu.mcis.cs310.tas_sp25.Badge;
import edu.jsu.mcis.cs310.tas_sp25.Department;
import edu.jsu.mcis.cs310.tas_sp25.Employee;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.time.DayOfWeek;

public class PunchDAO {
    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";
    private static final String QUERY_CREATE = "INSERT INTO event(terminalid,badgeid,timestamp,eventtypeid)VALUES(?,?,?,?)";
    private static final String QUERY_LIST = "SELECT * FROM event WHERE badgeid = ? ORDER BY timestamp ASC";
    private static final String QUERY_NEXT_DAY_PUNCHES = "SELECT * FROM event WHERE badgeid = ? AND timestamp > ? LIMIT 1";
    
        
    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    /**
     * A find method to find the Punch by the ID
     * @param id the ID of the Punch
     * @return Punch
     */
    public Punch find(Integer id) {

        Punch punch = null;
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

                    while (rs.next()) {
                         
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        int terminalId = rs.getInt("terminalid");

                        // Getting badge
                        String badgeid = rs.getString("badgeid");
                        Badge badge = daoFactory.getBadgeDAO().find(badgeid);

                        // Getting punch type 
                        EventType punchtype = EventType.values()[rs.getInt("eventtypeid")];

                        // Getting timestamp
                        LocalDateTime originaltimestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                        // Creating Punch object
                        punch = new Punch(id, terminalId, badge, originaltimestamp, punchtype);

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

        return punch;

    }
    /**
     * a create method that creates a new punch
     * @param punch the punch being created
     * @return punchID
     */
    public Integer create(Punch punch) {

        Integer punchId = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        EmployeeDAO employeedao = daoFactory.getEmployeeDAO();
        DepartmentDAO departmentdao = daoFactory.getDepartmentDAO();
        Employee emp = employeedao.find(punch.getBadge());
        
        Integer Empid =emp.getId();
        Department department = departmentdao.find(Empid);
        Integer terminalid = department.getTerminalId();
        
        
        
        
        if (Objects.equals(terminalid, punch.getTerminalid())){

            try {

                Connection conn = daoFactory.getConnection();

                if (conn.isValid(0)) {

                    ps = conn.prepareStatement(QUERY_CREATE,PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, punch.getTerminalid());
                    ps.setString(2, punch.getBadge().getId());
                    ps.setTimestamp(3, Timestamp.valueOf(punch.getOriginaltimestamp().withNano(0)));
                    ps.setInt(4, punch.getPunchtype().ordinal());


                    int rowAffected = ps.executeUpdate();

                    if (rowAffected == 1) {

                        rs = ps.getGeneratedKeys();

                        if (rs.next()) {
                            punchId = rs.getInt(1);

                        
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
    }
        return punchId;

    
    
    }
    

    /**
     * Returns a list of punches
     * @param badge the badge of the Employee
     * @param date the date of the Punch
     * @return list
     */
    public ArrayList<Punch> list(Badge badge, LocalDate date) {
        
        ArrayList<Punch> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setString(1, badge.getId());
                boolean hasResults = ps.execute();
                
                if (hasResults) {
                    rs = ps.getResultSet();

                    while (rs.next()) {
                        LocalDate l_date = rs.getTimestamp("timestamp").toLocalDateTime().toLocalDate();

                        if (l_date.equals(date)) {
                            int id = rs.getInt("id");
                            list.add(find(id));
                        }
                    }
                }
                
                // Check if the last punch of the day is CLOCK_IN
                if (!list.isEmpty() && list.get(list.size() - 1).getPunchtype() == EventType.CLOCK_IN) {
                    LocalDateTime lastPunchDate = list.get(list.size() - 1).getOriginaltimestamp();
                    Timestamp lastPunchTimestamp = Timestamp.valueOf(lastPunchDate);
                    
                    // Fetch punches from the next day
                    ps = conn.prepareStatement(QUERY_NEXT_DAY_PUNCHES);
                    ps.setString(1, badge.getId());
                    ps.setTimestamp(2, lastPunchTimestamp);
                    hasResults = ps.execute();
                    
                    if (hasResults) {
                        rs = ps.getResultSet();
                        
                        // Fetch the first CLOCK_OUT or TIMEOUT punch from the next day
                        while (rs.next()) {
                            
                            int id = rs.getInt(1);
                            Punch punch = find(id);
                            
                            if (punch.getPunchtype() == EventType.CLOCK_OUT || punch.getPunchtype() == EventType.TIME_OUT) {
                                list.add(punch);
                                break;  // Stop after adding the first CLOCK_OUT or TIMEOUT punch
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }

            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        
        return list;
        
        }
    }