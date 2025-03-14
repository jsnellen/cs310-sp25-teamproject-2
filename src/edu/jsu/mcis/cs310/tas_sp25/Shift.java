/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

/**
 *
 * @author brooklynleonard
 */





import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.Map;

public class Shift {
    private LocalTime shiftstart;
    private LocalTime stopTime;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private int totalMinutes;
    private int lunchMinutes;
    private String shiftType;
    private Duration graceperiod;
    private Duration dockpenalty;
    private Duration roundinterval;


    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Shift(Map<String, String> shiftData) {
        // Parse times
        this.shiftstart = LocalTime.parse(shiftData.get("start"), TIME_FORMATTER);
        this.stopTime = LocalTime.parse(shiftData.get("stop"), TIME_FORMATTER);
        this.lunchStart = LocalTime.parse(shiftData.get("lunchstart"), TIME_FORMATTER);
        this.lunchStop = LocalTime.parse(shiftData.get("lunchstop"), TIME_FORMATTER);

        // Calculate total shift minutes
        this.totalMinutes = (int) Duration.between(shiftstart, stopTime).toMinutes();

        // Calculate lunch minutes
        this.lunchMinutes = (int) Duration.between(lunchStart, lunchStop).toMinutes();

        // Determine shift type
        this.shiftType = determineShiftType();
    }

    private String determineShiftType() {
        if (shiftstart.equals(LocalTime.parse("07:00", TIME_FORMATTER)) && lunchStart.equals(LocalTime.parse("11:30", TIME_FORMATTER))) {
            return "Shift 1 Early Lunch";
        } else if (shiftstart.equals(LocalTime.parse("07:00", TIME_FORMATTER)) && lunchStart.equals(LocalTime.parse("12:00", TIME_FORMATTER))) {
            return "Shift 1";
        } else if (shiftstart.equals(LocalTime.parse("12:00", TIME_FORMATTER))) {
            return "Shift 2";
        }
        return "Unknown Shift";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(shiftType)
          .append(": ")
          .append(shiftstart.format(TIME_FORMATTER))
          .append(" - ")
          .append(stopTime.format(TIME_FORMATTER))
          .append(" (")
          .append(totalMinutes)
          .append(" minutes); Lunch: ")
          .append(lunchStart.format(TIME_FORMATTER))
          .append(" - ")
          .append(lunchStop.format(TIME_FORMATTER))
          .append(" (")
          .append(lunchMinutes)
          .append(" minutes)");
        
        return sb.toString();
    }

    // Getters (if needed)
    public LocalTime getStartTime() {
        return shiftstart;
    }

    public LocalTime getStopTime() {
        return stopTime;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getLunchMinutes() {
        return lunchMinutes;
    }

    public String getShiftType() {
        return shiftType;
    }

    public Duration getGracePeriod() {
        return graceperiod;
    }

    public Duration getDockPenalty() {
        return dockpenalty;
    }

    public Duration getRoundInterval() {
        return roundinterval;
    }
}