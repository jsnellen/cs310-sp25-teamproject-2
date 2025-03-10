package edu.jsu.mcis.cs310.tas_sp25;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    //adjust method
    public void adjust(Shift s){
        //extract shift start, stop, lunch times, grace period, dock penalty, and interval
        LocalTime shiftStart = s.getShiftStart();
        LocalTime shiftStop = s.getShiftStop();
        LocalTime lunchStart = s.getLunchStart();
        LocalTime lunchStop = s.getLunchStop();
        Duration gracePeriod = s.getGracePeriod();
        Duration dockPenalty = s.getDockPenalty();
        Duration roundInterval = s.getRoundInterval();
        
        LocalDateTime timestamp = this.originaltimestamp;
        LocalTime time = timestamp.toLocalTime();
        DayOfWeek dayOfWeek = timestamp.getDayOfWeek();
        
        adjustedTimestamp = originaltimestamp;
        adjustmenttype = PunchAdjustmentType.NONE;
        
        //Determine if the user is clocking in or clocking out
        boolean isClockIn = (punchtype == EventType.CLOCK_IN);
        boolean isClockOut = (punchtype == EventType.CLOCK_OUT);
        
        //Weekends: no shift rules apply, only rounding
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
            //
            return;
        }

        //Rule 1: Employee clocks in early or clocks out late
        if (isClockIn && time.isBefore(shiftStart) &&
            Duration.between(time, shiftStart).compareTo(roundInterval)<= 0){
            adjustedTimestamp = timestamp.with(shiftStart);
           adjustmenttype = PunchAdjustmentType.SHIFT_START;
            
        }
        if (isClockOut && time.isAfter(shiftStop) &&
            Duration.between(time, shiftStop).compareTo(roundInterval) <= 0){
            adjustedTimestamp = timestamp.with(shiftStop);
            adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
            return;
        }
        
        //Rule 2: Employee clocks out for lunch too early or late
        if (isClockIn && time.equals(lunchStart)){
            adjustedTimestamp = timestamp.with(lunchStart);
           adjustmenttype = PunchAdjustmentType.LUNCH_START;
        }
        
        if (isClockIn && time.equals(lunchStop)){
            adjustedTimestamp = timestamp.with(lunchStop);
           adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
        }
           
        //Rule 3: Grace period for an employee who clocks in late/early within the grace period
        //Rule 4: Employee gets a dock penalty if their punch exceeds the grace period
        //Rule 5: If no rules apply, the punch is rounded to the nearest increment of the interval
        //Rule 6: No adjustment
        
        
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
    
    public String printOriginal(){
        
    StringBuilder s = new StringBuilder();
    
    s.append("Punch ID: ").append(id).append(" ");
    s.append("Terminal ID: ").append(terminalid).append(", ");
    s.append("Badge: ").append(badge).append(", ");
    s.append("Punch Type: ").append(punchtype).append(", ");
    s.append("Original Timestamp: ").append(originaltimestamp).append(")");
    
    return s.toString();
    }
    
    public String printAdjusted(){
        
        StringBuilder s = new StringBuilder();
        
        if(adjustedTimestamp != null){
           s.append("Punch ID: ").append(id).append(" ");
           s.append("Terminal ID: ").append(terminalid).append(", ");
           s.append("Badge: ").append(badge).append(", ");
           s.append("Adjusted Timestamp: ").append(adjustedTimestamp).append(", ");
           s.append("Adjustment Type: ").append(adjustmenttype).append(")");
        }else{
            s.append("No adjustment for Punch ID: ").append(id);
        }
        return s.toString();
    }
}
