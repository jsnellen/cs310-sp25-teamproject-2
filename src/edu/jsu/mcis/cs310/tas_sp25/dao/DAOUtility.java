package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.EventType;
import edu.jsu.mcis.cs310.tas_sp25.Punch;  
import edu.jsu.mcis.cs310.tas_sp25.PunchAdjustmentType;
import edu.jsu.mcis.cs310.tas_sp25.Shift;

import edu.jsu.mcis.cs310.tas_sp25.Punch;


/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {


    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        LocalDateTime shiftStart = null;
        LocalDateTime shiftStop = null;
        boolean lunchDeductible = false;
        long totalWorkedMinutes = 0;

        for (Punch punch : dailypunchlist) {
            EventType eventType = punch.getPunchtype();
            LocalDateTime punchTimestamp = punch.getAdjustedTimestamp();

            switch (eventType) {
                case CLOCK_IN:
                    shiftStart = punchTimestamp;
                    shiftStop = null; // Reset shift stop time
                    lunchDeductible = false; // Reset lunch deductible flag
                    break;

                case CLOCK_OUT:
                    shiftStop = punchTimestamp;
                    break;

                case TIME_OUT:
                    // Ignore time-out punch and reset shift stop time
                    shiftStop = null;
                    break;
                default:
                    break;
            }

            if (shiftStart != null && shiftStop != null) {
                long shiftDurationMinutes = Duration.between(shiftStart, shiftStop).toMinutes();

                // Check if shift duration exceeds lunch threshold for lunch deduction
                if (shiftDurationMinutes > shift.getLunchThreshold()) {
                    lunchDeductible = true;
                }

                if (eventType == EventType.CLOCK_OUT && lunchDeductible) {
                    // Calculate lunch break duration and deduct from total worked minutes
                    long lunchDurationMinutes = shift.getLunchDuration().toMinutes();
                    totalWorkedMinutes += shiftDurationMinutes - lunchDurationMinutes;
                    
                } else {
                    totalWorkedMinutes += shiftDurationMinutes;
                }

                // Reset shift start and stop times for the next shift
                shiftStart = null;
                shiftStop = null;
                lunchDeductible = false;
            }
        }

        return (int) totalWorkedMinutes;
        }
    
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
    // List to hold JSON data for each punch
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        // DateTimeFormatter to format timestamp
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");

        // Loop through each Punch object in the list
        for (Punch punch : dailypunchlist) {
            // HashMap to store punch data as key-value pairs
            HashMap<String, String> punchData = new HashMap<>();

            // Populate punchData with punch details
            punchData.put("id", String.valueOf(punch.getId()));  
            punchData.put("badgeid", String.valueOf(punch.getBadge().getId()));  
            punchData.put("terminalid", String.valueOf(punch.getTerminalid()));  
            punchData.put("punchtype", String.valueOf(punch.getPunchtype()));  
            punchData.put("adjustmenttype", String.valueOf(punch.getAdjustmentType()));  
            punchData.put("originaltimestamp", punch.getOriginaltimestamp().format(format).toUpperCase());  
            punchData.put("adjustedtimestamp", punch.getAdjustedTimestamp().format(format).toUpperCase());

            // Add punchData to jsonData list
            jsonData.add(punchData);
        }

        // Serialize jsonData to JSON string using Jsoner library
        String json = Jsoner.serialize(jsonData);

        // Return the JSON string
        return json;
    }

}