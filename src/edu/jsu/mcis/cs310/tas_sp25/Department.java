package edu.jsu.mcis.cs310.tas_sp25;
//A model class defines the properties and methods of a data structure
//It should represent the structire of the data / correspond to a table in the database
//This model class will be used to represent !departments!
public class Department {
    //Variables
    private int id;
    private String description;
    private int terminalid;
    
    //Constructor
    public Department(int id, String description, int terminalid){
        this.id = id;
        this.description = description;
        this.terminalid = terminalid;
    }
    
    //Getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getTerminalid() {
        return terminalid;
    }

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", description=" + description + ", terminalid=" + terminalid + '}';
    }
    
}
