package edu.jsu.mcis.cs310.tas_sp25;

/**
 * Represents a department containing the id, terminal id, and its description
 * 
 */
public class Department {
    private  int id, terminalid;
    private  String description;

    //Constructor for creating a Department object 
    /**
     * Constructs a Department object with id, description, and terminal id
     * @param id The ID of the department
     * @param description The description of the department
     * @param terminalid The ID of the terminal associated with the department
     */
    public Department(int id, String description, int terminalid) {
        this.id = id;
        this.description = description;
        this.terminalid = terminalid;
    }
    
   //Getter Methods
    /**
     * Gets the ID of the department.
     * @return The ID of the department.
     */
    public int getId(){
        return id;
    }
    
    /**
     * Gets the description of the department.
     * @return The description of the department.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Gets the terminal ID associated with the department.
     * @return The terminal ID.
     */
    public int getTerminalid(){
        return terminalid;
    }
    
    /**
     * Generates a string representation of the Department object, including ID, description, and terminal ID.
     * @return A string representation of the Department object
     */
    @Override
    public String toString(){
        StringBuilder dpString = new StringBuilder();
        
        dpString.append('#').append(id).append(' ');
        dpString.append('(').append(description).append(')');
        dpString.append(',').append(" Terminal ID: ").append(terminalid);
        
        return dpString.toString();
        
    }
}
