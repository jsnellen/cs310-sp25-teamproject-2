/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalDateTime;

/**
 *
 * @author alexi
 * Purpose: represent a timestamp when an employee clocks in/out 
 * using a time clock
 */
public class Punch {
    //ID for the punch (initially null)
    private int id;
    
    //The ID of the time clock
    private int terminalID;
    
    //Employee badge info
    private Badge badge;
    
    //Original timestamp
    private LocalDateTime originalTimestamp;
    
    //Adjusted timestamp (initially null)
    private LocalDateTime adjustedTimestamp;
     
    //The type of punch: clock in, clock out, ect.
    private EventType punchType;
    
    
    //Constructor for new Punch objects
    public Punch(int terminalID, Badge badge, EventType punchType){
        this.terminalID = terminalID;
        this.badge = badge;
        this.punchType = punchType;
        //when the punch occurred in real time
        this.originalTimestamp = LocalDateTime.now();
        //no adjustment is made yet
        this.adjustedTimestamp = null;
    }
    
    public Punch(int id, int terminalID, Badge badge, LocalDateTime originalTimeStamp,
    EventType punchType){
        this.id = id;
        this.terminalID = terminalID;
        this.badge = badge;
        this.punchType = punchType;
        this.originalTimestamp = originalTimestamp;
        this.adjustedTimestamp = null;
    }
    
}
