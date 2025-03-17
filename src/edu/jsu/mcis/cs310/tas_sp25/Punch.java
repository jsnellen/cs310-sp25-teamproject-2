package edu.jsu.mcis.cs310.tas_sp25;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    
    public void adjust(Shift s) {        
        LocalTime shiftStart = s.getStartTime();
        LocalTime shiftStop = s.getStopTime();
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

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            adjustedTimestamp = timestamp.withSecond(0).withNano(0);
            return;
        }

        boolean isClockIn = (punchtype == EventType.CLOCK_IN);
        boolean isClockOut = (punchtype == EventType.CLOCK_OUT);

        if (isClockIn && time.isBefore(shiftStart) && Duration.between(time, shiftStart).compareTo(roundInterval) <= 0) {
            adjustedTimestamp = timestamp.with(shiftStart);
            adjustmenttype = PunchAdjustmentType.SHIFT_START;
            return;
        }

        if (isClockOut && time.isAfter(shiftStop) && Duration.between(time, shiftStop).compareTo(roundInterval) <= 0) {
            adjustedTimestamp = timestamp.with(shiftStop);
            adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
            return;
        }

        if (isClockIn && time.equals(lunchStart)) {
            adjustedTimestamp = timestamp.with(lunchStart);
            adjustmenttype = PunchAdjustmentType.LUNCH_START;
            return;
        }

        if (isClockIn && time.equals(lunchStop)) {
            adjustedTimestamp = timestamp.with(lunchStop);
            adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
            return;
        }

        if (isClockIn && time.isAfter(shiftStart) && Duration.between(shiftStart, time).compareTo(gracePeriod) <= 0) {
            adjustedTimestamp = timestamp.with(shiftStart);
            adjustmenttype = PunchAdjustmentType.SHIFT_START;
            return;
        }

        if (isClockIn && time.isAfter(shiftStart) && Duration.between(shiftStart, time).compareTo(gracePeriod) > 0) {
            adjustedTimestamp = timestamp.with(shiftStart.plus(dockPenalty));
            adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
            return;
        }

        int roundedMinutes = (time.getMinute() / (int)roundInterval.toMinutes()) * (int)roundInterval.toMinutes();
        adjustedTimestamp = timestamp.withMinute(roundedMinutes).withSecond(0).withNano(0);
        adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
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
