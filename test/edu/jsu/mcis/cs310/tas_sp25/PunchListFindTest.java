package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import java.time.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class PunchListFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindPunchList1() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate ts = LocalDate.of(2018, Month.SEPTEMBER, 17);

        Badge b = badgeDAO.find("67637925");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(4716));
        p2.add(punchDAO.find(4811));
        p2.add(punchDAO.find(4813));
        p2.add(punchDAO.find(4847));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testFindPunchList2() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate ts = LocalDate.of(2018, Month.SEPTEMBER, 27);

        Badge b = badgeDAO.find("87176FD7");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        
        p2.add(punchDAO.find(6089));
        p2.add(punchDAO.find(6112));
        p2.add(punchDAO.find(6118));
        p2.add(punchDAO.find(6129));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testFindPunchList3() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate ts = LocalDate.of(2018, Month.SEPTEMBER, 5);

        Badge b = badgeDAO.find("95497F63");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(3463));
        p2.add(punchDAO.find(3482));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testFindPunchList4(){
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        
        LocalDate ts = LocalDate.of(2018, Month.AUGUST, 1);
        
        Badge b = badgeDAO.find("4E6E296E");
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        ArrayList<Punch> p2 = new ArrayList<>();
        
        p2.add(punchDAO.find(147));
        p2.add(punchDAO.find(237));

        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }
        
         assertEquals(s2.toString(), s1.toString());

    }
    
    @Test
    public void testFindPunchList5(){
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        
        LocalDate ts = LocalDate.of(2018, Month.AUGUST, 10);
        
        Badge b = badgeDAO.find("9E06A774");
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        ArrayList<Punch> p2 = new ArrayList<>();
        
        p2.add(punchDAO.find(960));
        p2.add(punchDAO.find(1024));

        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }
        
         assertEquals(s2.toString(), s1.toString());

    }
    
    @Test
    public void testFindPunchList6(){
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        
        LocalDate ts = LocalDate.of(2018, Month.SEPTEMBER, 28);
        
        Badge b = badgeDAO.find("8709982E");
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        ArrayList<Punch> p2 = new ArrayList<>();
        
        p2.add(punchDAO.find(6877));
        p2.add(punchDAO.find(6878));

        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }
        
         assertEquals(s2.toString(), s1.toString());

    }
    
        @Test
        public void testFindPunchList7(){
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        
        LocalDate ts = LocalDate.of(2018, Month.AUGUST, 01);
        
        Badge b = badgeDAO.find("28DC3FB8");
        
        ArrayList<Punch> p1 = punchDAO.list(b, ts);
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        ArrayList<Punch> p2 = new ArrayList<>();
        
        p2.add(punchDAO.find(150));
        p2.add(punchDAO.find(219));

        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }
        
         assertEquals(s2.toString(), s1.toString());

    }
}
