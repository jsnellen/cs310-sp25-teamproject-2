/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.jsu.mcis.cs310.tas_sp25;

/**
 *
 * @author brooklynleonard
 */
import java.time.LocalDateTime;
import java.util.Map;

public class Shift {
    private String startTime;
    private String stopTime;
    private int lunchDuration;  // in minutes
    private int shiftDuration;  // in minutes

    public Shift(Map<String, String> shiftData) {
        if (shiftData == null) {
            throw new IllegalArgumentException("Shift data cannot be null.");
        }

        this.startTime = shiftData.getOrDefault("startTime", "00:00");
        this.stopTime = shiftData.getOrDefault("stopTime", "00:00");
        
        // Convert values from String to int with error handling
        try {
            this.lunchDuration = Integer.parseInt(shiftData.getOrDefault("lunchDuration", "0"));
            this.shiftDuration = Integer.parseInt(shiftData.getOrDefault("shiftDuration", "0"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in shift data.");
        }
    }

    // Getters for accessing the data
    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public int getLunchDuration() {
        return lunchDuration;
    }

    public int getShiftDuration() {
        return shiftDuration;
    }

    @Override
    public String toString() {
        return "Shift [Start Time: " + startTime + ", Stop Time: " + stopTime + 
               ", Lunch Duration: " + lunchDuration + " min, Shift Duration: " + shiftDuration + " min]";
    }
}