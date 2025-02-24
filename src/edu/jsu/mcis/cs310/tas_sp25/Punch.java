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
    private int terminalid;
    
    //Employee badge info
    private Badge badge;
    
    //Original timestamp
    private LocalDateTime originaltimestamp;
    
    //Adjusted timestamp (initially null)
    private LocalDateTime adjustedTimestamp;
     
    //The type of punch: clock in, clock out, ect.
    private EventType punchtype;
    
    private PunchAdjustmentType adjustmenttype;
    
    
    //Constructor for new Punch objects
    public Punch(int terminalid, Badge badge, EventType punchType){
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchType;
        //when the punch occurred in real time
        this.originaltimestamp = LocalDateTime.now();
        //no adjustment is made yet
        this.adjustedTimestamp = null;
        this.id = null;
        this.adjustmenttype = PunchAdjustmentType.NONE;
    }
    
    public Punch(int id, int terminalID, Badge badge, LocalDateTime originaltimestamp,
    EventType punchType){
        this.id = id;
        this.terminalid = terminalID;
        this.badge = badge;
        this.punchtype = punchType;
        this.originaltimestamp = originaltimestamp;
        this.adjustedTimestamp = null;
        this.adjustmenttype = PunchAdjustmentType.NONE;

    }
    
    //Getters and setters

    public Integer getId() {
        return id;
    }
    
    public int getTerminalid() {
        return terminalid;
    }

    public Badge getBadge() {
        return badge;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }

    public LocalDateTime getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    public void setAdjustedTimestamp(LocalDateTime adjustedTimestamp) {
        this.adjustedTimestamp = adjustedTimestamp;
    }

    public EventType getPunchtype() {
        return punchtype;
    }

    public PunchAdjustmentType getAdjustmentType(){
        return adjustmenttype;
    }
    
    public void setAdjustmentType(PunchAdjustmentType adjustmentType){
        this.adjustmenttype = adjustmentType;
    }
    
    public void printOriginal(){
    System.out.println("Punch ID: " + id + ", Terminal ID: " + terminalid + ", Badge: "
        + badge + ", Punch Type: " + punchtype + ", Original Timestamp: " 
        + originaltimestamp + ", Adjustment Type: " + adjustmenttype);
    }
    
    public void printAdjusted(){
        if(adjustedTimestamp != null){
            System.out.println("Punch ID: " + id + ", Terminal ID: " + terminalid + ", Badge: "
            + badge + ", Punch Type: " + punchtype + ", Adjusted Timestamp: " 
            + adjustedTimestamp + ", Adjustment Type: " + adjustmenttype);
        }else{
            System.out.println("No adjustment for Punch ID: " + id);
        }
   }
}
