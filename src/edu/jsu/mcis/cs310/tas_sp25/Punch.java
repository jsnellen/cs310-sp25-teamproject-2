/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;
import java.time.LocalDateTime;

/*
 * Purpose: represent a timestamp when an employee clocks in/out 
 * using a time clock
 */
public class Punch {
    //ID for the punch (initially null)
    private Integer id;
    
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
    
    private PunchAdjustmentType adjustmentType;
    
    
    //Constructor for new Punch objects
    public Punch(int terminalID, Badge badge, EventType punchType){
        this.terminalID = terminalID;
        this.badge = badge;
        this.punchType = punchType;
        //when the punch occurred in real time
        this.originalTimestamp = LocalDateTime.now();
        //no adjustment is made yet
        this.adjustedTimestamp = null;
        this.id = null;
        this.adjustmentType = PunchAdjustmentType.NONE;
    }
    
    public Punch(int id, int terminalID, Badge badge, LocalDateTime originalTimeStamp,
    EventType punchType){
        this.id = id;
        this.terminalID = terminalID;
        this.badge = badge;
        this.punchType = punchType;
        this.originalTimestamp = originalTimestamp;
        this.adjustedTimestamp = null;
        this.adjustmentType = PunchAdjustmentType.NONE;

    }
    
    //Getters and setters

    public Integer getId() {
        return id;
    }
    
    public int getTerminalID() {
        return terminalID;
    }

    public Badge getBadge() {
        return badge;
    }

    public LocalDateTime getOriginalTimestamp() {
        return originalTimestamp;
    }

    public LocalDateTime getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    public void setAdjustedTimestamp(LocalDateTime adjustedTimestamp) {
        this.adjustedTimestamp = adjustedTimestamp;
    }

    public EventType getPunchType() {
        return punchType;
    }

    public PunchAdjustmentType getAdjustmentType(){
        return adjustmentType;
    }
    
    public void setAdjustmentType(PunchAdjustmentType adjustmentType){
        this.adjustmentType = adjustmentType;
    }
    
    public void printOriginal(){
    System.out.println("Punch ID: " + id + ", Terminal ID: " + terminalID + ", Badge: "
        + badge + ", Punch Type: " + punchType + ", Original Timestamp: " 
        + originalTimestamp + ", Adjustment Type: " + adjustmentType);
    }
    
    public void printAdjusted(){
        if(adjustedTimestamp != null){
            System.out.println("Punch ID: " + id + ", Terminal ID: " + terminalID + ", Badge: "
            + badge + ", Punch Type: " + punchType + ", Adjusted Timestamp: " 
            + adjustedTimestamp + ", Adjustment Type: " + adjustmentType);
        }else{
            System.out.println("No adjustment for Punch ID: " + id);
        }
   }
}
