/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

/**
 * Represents a shift containing the id, description, start and end of a shift, the round interval, grace period, dock penalty, lunch start and stop, lunch threshold, lunch duration, and shift duration
 */
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

public class Shift {
    private Integer id;
    private String description;
    private LocalTime shiftStart;
    private LocalTime shiftStop;
    private Integer roundInterval;
    private Integer gracePeriod;
    private Integer dockPenalty;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private Integer lunchThreshold;
    private Duration lunchDuration;
    private Duration shiftDuration;

    /**
     * Constructor for create a shift Object.
     * @param shiftInfo a Map String, String for the shift related information.
     */
    public Shift(HashMap<String, String> shiftInfo) {
        // Retrieve values from the map and convert them to their native types
        this.id = Integer.valueOf((String) shiftInfo.get("id"));
        this.description = (String) shiftInfo.get("description");
        this.shiftStart = LocalTime.parse((String) shiftInfo.get("shiftStart"));
        this.shiftStop = LocalTime.parse((String) shiftInfo.get("shiftStop"));
        this.roundInterval = Integer.parseInt((String) shiftInfo.get("roundInterval"));
        this.gracePeriod = Integer.parseInt((String) shiftInfo.get("gracePeriod"));
        this.dockPenalty = Integer.parseInt((String) shiftInfo.get("dockPenalty"));
        this.lunchStart = LocalTime.parse((String) shiftInfo.get("lunchStart"));
        this.lunchStop = LocalTime.parse((String) shiftInfo.get("lunchStop"));
        this.lunchThreshold = Integer.parseInt((String) shiftInfo.get("lunchThreshold"));
        this.lunchDuration = Duration.between(lunchStart,lunchStop); 
        // Check for time duration between differant dates
        if (Duration.between(shiftStart,shiftStop).isNegative()) {
            this.shiftDuration = Duration.between(shiftStart,shiftStop).plusDays(1);
        } else { 
            this.shiftDuration = Duration.between(shiftStart,shiftStop);
        } 
        
    }

    // Getters methods
    /**
     * Gets the ID of the shift.
     * @return The ID of the shift.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Gets the description of the shift.
     * @return the description of the shift.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the time for the start of the shift.
     * @return Start time for the shift.
     */
    public LocalTime getShiftStart() {
        return shiftStart;
    }
    /**
     * Gets the time for the end of the shift.
     * @return End time for the shift.
     */
    public LocalTime getShiftStop() {
        return shiftStop;
    }
    /**
     * Gets the round interval for the shift.
     * @return The round interval for the shift.
     */
    public Integer getRoundInterval() {
        return roundInterval;
    }
    /**
     * Gets the grace period for the shift.
     * @return The grace period for the shift.
     */
    public Integer getGracePeriod() {
        return gracePeriod;
    }
    /**
     * Gets the dock penalty for the shift.
     * @return The dock penalty for the shift.
     */
    public Integer getDockPenalty() {
        return dockPenalty;
    }
    /**
     * Gets the time for the start of the lunch period of the shift.
     * @return Start time for the lunch period.
     */
    public LocalTime getLunchStart() {
        return lunchStart;
    }
    /**
     * Gets the time for the end of the lunch period of the shift.
     * @return End time for the lunch period.
     */
    public LocalTime getLunchStop() {
        return lunchStop;
    }
    /**
     * Gets the lunch threshold for the shift.
     * @return the lunch threshold for the shift.
     */
    public Integer getLunchThreshold() {
        return lunchThreshold;
    }
    /**
     * Gets the lunch duration of the shift.
     * @return The lunch duration of the shift.
     */
    public Duration getLunchDuration() {
        return lunchDuration;
    }
    /**
     * Gets the duration of the shift.
     * @return The duration of the shift.
     */
    public Duration getShiftDuration() {
        return shiftDuration;
    }
    
    /**
     * Generates a string representation of the Shift object, including its description, shift time, and lunch breaks.
     * @return A string representation of the Shift object.
     */
    @Override
    public String toString() {
    StringBuilder builder = new StringBuilder();
    
    builder.append(description).append(": ")
           .append(shiftStart).append(" - ")
           .append(shiftStop).append(" (")
           .append(shiftDuration.toMinutes()).append(" minutes); Lunch: ")
           .append(lunchStart).append(" - ")
           .append(lunchStop).append(" (")
           .append(lunchDuration.toMinutes()).append(" minutes)");
      
    
    return builder.toString();
    
    }
}
