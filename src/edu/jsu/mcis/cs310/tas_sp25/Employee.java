package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a employee containing the id, first name, middle name, last name, 
 * active time, badge, department, shift, and employee type.
 * 
 */
public class Employee {   
    // Classes
    private int id;
    private String firstname, middlename, lastname;
    private LocalDateTime active;
    private Badge badge;
    private Department department;
    private Shift shift;
    private EmployeeType employeeType;

// Constructor for creating an Employee object with specified parameters.
    /**
     * Constructs a Employee object with id, first name, middle name, last name, 
     * badge, department, shift, employeeType.
     * @param id The ID of the Employee.
     * @param firstname The first name of the Employee.
     * @param middlename The middle name of the Employee.
     * @param lastname The last name of the Employee.
     * @param active The active time of the Employee.
     * @param badge The badge of the Employee.
     * @param department The department of the Employee.
     * @param shift The shift of the Employee.
     * @param employeeType The employeeType of the Employee.
     */
public Employee(int id, String firstname, String middlename, String lastname, LocalDateTime active, Badge badge, 
            Department department, Shift shift, EmployeeType employeeType) 
    {
        // gathers information for the classes
        
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.active = active;
        this.badge = badge;
        this.department = department;
        this.shift = shift;
        this.employeeType=employeeType;
    }
    
    //Getter Methods
    /**
     * Gets the ID of the Employee.
     * @return The ID of the Employee.
     */
    public int getId() {
        return id;
    }
    /**
     * Gets the first name of the Employee.
     * @return The first name of the Employee.
     */
    public String getFirstname() {
        return firstname;
    }
    /**
     * Gets the middle name of the Employee.
     * @return The middle name of the Employee.
     */
    public String getMiddlename() {
        return middlename;
    }
    /**
     * Gets the last name of the Employee.
     * @return The last name of the Employee.
     */
    public String getLastname() {
        return lastname;
    }
    /**
     * Gets the active time of the Employee.
     * @return The active time of the Employee.
     */
    public LocalDateTime getActive() {
        return active;
    }
    /**
     * Gets the badge of the Employee.
     * @return The badge of Employee.
     */
    public Badge getBadge() {
        return badge;
    }
    /**
     * Gets the department of the Employee.
     * @return The department of Employee.
     */
    public Department getDepartment() {
        return department;
    }
    /**
     * Gets the shift of the Employee.
     * @return The shift of the Employee.
     */
    public Shift getShift() {
        return shift;
    }
    /**
     * Gets the employee type of the Employee
     * @return The employee type of the Employee
     */
    public EmployeeType getEmployeeType() {
        return employeeType;
    }
    
  
    
    /**
     * Generates a string representation of the Employee object, including ID, first name, middle name, last name, badge, type, department, active status.
     * @return A string representation of the Employee object.
     */
    @Override
    public String toString() {
        
       StringBuilder employeestring = new StringBuilder();
       
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
         employeestring.append("ID #").append(id)
                       .append(": ")
                       .append(lastname)
                       .append(", ")
                       .append(firstname)
                       .append(" ")
                       .append(middlename)
                       .append(" (#")
                       .append(badge.getId())
                       .append("), Type: ") 
                       .append(employeeType)
                       .append(", Department: ")
                       .append(department.getDescription())
                       .append(", Active: ")
                       .append(active.format(format));
            
         
        return employeestring.toString();
        
    }
}