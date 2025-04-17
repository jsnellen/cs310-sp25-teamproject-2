/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import java.time.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
/**
 *
 * @author alexi
 */
public class PunchDateRangeTest {
    private DAOFactory daoFactory;
    
  @Before
  public void setup(){
      daoFactory = new DAOFactory("tas.jdbc");
  }
  
  @Test
  public void testFindPunchListRange1(){
      BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
      PunchDAO punchDAO = daoFactory.getPunchDAO();
      
      Badge b = badgeDAO.find("4DAC9951");
      
      LocalDate begin = LocalDate.of(2018, Month.AUGUST, 1);
      LocalDate end = LocalDate.of(2018, Month.AUGUST, 7);
      
    ArrayList<Punch> p1 = punchDAO.list(b, begin, end);

      ArrayList<Punch> p2 = new ArrayList<>();
      p2.add(punchDAO.find(201));
      p2.add(punchDAO.find(221));
      p2.add(punchDAO.find(318));
      p2.add(punchDAO.find(362));
      p2.add(punchDAO.find(434));
      p2.add(punchDAO.find(438));
      p2.add(punchDAO.find(654));
      p2.add(punchDAO.find(682));

      StringBuilder s1 = new StringBuilder();
      for(Punch p : p1){
          s1.append(p.printOriginal()).append("\n");
      }
      
       StringBuilder s2 = new StringBuilder();
      for(Punch p : p2){
          s2.append(p.printOriginal()).append("\n");
      }
      
      assertEquals(s2.toString(), s1.toString());
  }
  
 @Test
  public void testFindPunchListRange2(){
      BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
      PunchDAO punchDAO = daoFactory.getPunchDAO();
      
      Badge b = badgeDAO.find("8C4CE4AC");
      
      LocalDate begin = LocalDate.of(2018, Month.AUGUST, 2);
      LocalDate end = LocalDate.of(2018, Month.SEPTEMBER, 22);
      
    ArrayList<Punch> p1 = punchDAO.list(b, begin, end);

      ArrayList<Punch> p2 = new ArrayList<>();
      p2.add(punchDAO.find(272));
      p2.add(punchDAO.find(379));
      p2.add(punchDAO.find(5367));
      p2.add(punchDAO.find(5413));
      p2.add(punchDAO.find(5479));
      p2.add(punchDAO.find(5538));

      StringBuilder s1 = new StringBuilder();
      for(Punch p : p1){
          s1.append(p.printOriginal()).append("\n");
      }
      
       StringBuilder s2 = new StringBuilder();
      for(Punch p : p2){
          s2.append(p.printOriginal()).append("\n");
      }
      
      assertEquals(s2.toString(), s1.toString());
  }
}
