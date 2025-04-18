package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import org.junit.*;
import static org.junit.Assert.*;

public class EmployeeFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindEmployee1() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        /* Retrieve Employee from Database (by ID) */

        Employee e1 = employeeDAO.find(14);

        /* Compare to Expected Values */

        assertEquals("ID #14: Donaldson, Kathleen C (#229324A4), Type: Full-Time, Department: Press, Active: 02/02/2017", e1.toString());

    }

    @Test
    public void testFindEmployee2() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Employee from Database (by badge) */

        Badge b = badgeDAO.find("ADD650A8");
        Employee e2 = employeeDAO.find(b);

        /* Compare to Expected Values */

        assertEquals("ID #82: Taylor, Jennifer T (#ADD650A8), Type: Full-Time, Department: Office, Active: 02/13/2016", e2.toString());

    }

    @Test
    public void testFindEmployee3() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        /* Retrieve Employee from Database (by ID) */

        Employee e3 = employeeDAO.find(127);

        /* Compare to Expected Values */

        assertEquals("ID #127: Elliott, Nancy L (#EC531DE6), Type: Temporary / Part-Time, Department: Shipping, Active: 09/22/2015", e3.toString());

    }

    @Test
    public void testFindEmployee4() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Employee from Database (by badge) */

        Badge b = badgeDAO.find("C1E4758D");
        Employee e4 = employeeDAO.find(b);

        /* Compare to Expected Values */

        assertEquals("ID #93: Leist, Rodney J (#C1E4758D), Type: Temporary / Part-Time, Department: Warehouse, Active: 10/09/2015", e4.toString());

    }

    @Test
    public void testFindEmployee5() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        /* Retrieve Employee from Database (by ID) */
        Employee e5 = employeeDAO.find(20);

        /* Compare to Expected Values */
        assertEquals("ID #20: Eaton, Curtis M (#2A5620A0), Type: Temporary / Part-Time, Department: Cleaning, Active: 10/16/2015", e5.toString());

    }

    @Test
    public void testFindEmployee6() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Employee from Database (by badge) */
        Badge b = badgeDAO.find("4382D92D");
        Employee e6 = employeeDAO.find(b);

        /* Compare to Expected Values */
        assertEquals("ID #32: Alvarez, Laurie J (#4382D92D), Type: Full-Time, Department: Office, Active: 10/09/2015", e6.toString());

    }

    @Test
    public void testFindEmployee7() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        /* Retrieve Employee from Database (by ID) */
        Employee e7 = employeeDAO.find(75);

        /* Compare to Expected Values */
        assertEquals("ID #75: Lilly, James M (#9BFCB537), Type: Full-Time, Department: Shipping, Active: 09/29/2015", e7.toString());

    }

    @Test
    public void testFindEmployee8() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Employee from Database (by badge) */
        Badge b = badgeDAO.find("8709982E");
        Employee e8 = employeeDAO.find(b);

        /* Compare to Expected Values */
        assertEquals("ID #57: Dent, Judy E (#8709982E), Type: Full-Time, Department: Assembly, Active: 06/27/2016", e8.toString());

    }

    @Test
    public void testFindEmployee9() {

        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        /* Retrieve Employee from Database (by ID) */
        Employee e9 = employeeDAO.find(93);

        /* Compare to Expected Values */
        assertEquals("ID #93: Leist, Rodney J (#C1E4758D), Type: Temporary / Part-Time, Department: Warehouse, Active: 10/09/2015", e9.toString());

    }


}