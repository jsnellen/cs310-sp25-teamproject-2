package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import edu.jsu.mcis.cs310.tas_sp25.Shift;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {
    public static int calculateTotalMinutes(ArrayList<Punch> punchList, Shift shift) {
        int totalMinutes = 0;
        
        if (punchList == null || punchList.isEmpty()) {
            return totalMinutes; // Return 0 if no punches are found
        }
        
        // Sort punches by timestamp to ensure correct pairing
        punchList.sort(Comparator.comparing(Punch::getAdjustedTimestamp));
        
        for (int i = 0; i < punchList.size(); i += 2) {
            if (i + 1 < punchList.size()) { // Ensure we have a valid pair (IN/OUT)
                Punch inPunch = punchList.get(i);
                Punch outPunch = punchList.get(i + 1);
                
                // Calculate worked minutes
                long minutesWorked = Duration.between(
                        inPunch.getAdjustedTimestamp(),
                        outPunch.getAdjustedTimestamp()
                ).toMinutes();
                
                totalMinutes += (int) minutesWorked;
            }
        }
        
        return totalMinutes;
    }
}

