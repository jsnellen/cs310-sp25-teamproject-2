package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.util.ArrayList;
import java.util.HashMap;
import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_sp25.Punch;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import edu.jsu.mcis.cs310.tas_sp25.EventType; // your enum for punch types
import java.time.Duration;

public class DAOUtility {

    /**
     * Converts a list of Punch objects into a JSON string.
     * Each Punch's data is placed into a HashMap with keys:
     * "id", "badgeid", "terminalid", "punchtype", "adjustmenttype",
     * "originaltimestamp", and "adjustedtimestamp".
     *
     * @param dailypunchlist an ArrayList of Punch objects
     * @return a JSON string representing the list of punches
     */
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (Punch p : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", String.valueOf(p.getId()));
            // Assuming p.getBadge() returns a Badge that has a getId() method:
            punchData.put("badgeid", p.getBadge().getId());
            punchData.put("terminalid", String.valueOf(p.getTerminalid()));
            punchData.put("punchtype", p.getPunchtype().toString());
            punchData.put("adjustmenttype", p.getAdjustmentType().toString());
            punchData.put("originaltimestamp", p.printOriginal());
            punchData.put("adjustedtimestamp", p.printAdjusted());
            
            jsonData.add(punchData);
        }
        
        return Jsoner.serialize(jsonData);
    }

    /**
     * Calculates the total minutes accrued for a given day based on a list of Punch objects and a Shift.
     * It totals the minutes between CLOCK_IN and CLOCK_OUT punches (ignoring any pairs involving TIME_OUT).
     * If the total minutes exceed or equal the lunch threshold defined in the Shift, the lunch duration is deducted.
     *
     * @param dailypunchlist an ArrayList of Punch objects representing a single day (already adjusted)
     * @param shift a Shift object containing shift rules (including lunch threshold and duration)
     * @return the total number of minutes accrued for the day
     */
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        int totalMinutes = 0;
        Punch inPunch = null;
        
        // Iterate through the punches (assumed to be in chronological order)
        for (Punch punch : dailypunchlist) {
            if (punch.getPunchtype() == EventType.CLOCK_IN) {
                // Begin a new work period
                inPunch = punch;
            }
            else if (punch.getPunchtype() == EventType.CLOCK_OUT) {
                // End of a work period: if we have a valid "clock in" then calculate the duration.
                if (inPunch != null) {
                    long minutes = Duration.between(inPunch.getAdjustedTimestamp(), punch.getAdjustedTimestamp()).toMinutes();
                    totalMinutes += (int) minutes;
                    inPunch = null;
                }
            }
            else if (punch.getPunchtype() == EventType.TIME_OUT) {
                // If the punch is a TIME_OUT, we do not count that period.
                inPunch = null; // reset any pending CLOCK_IN
            }
        }
        
        // Apply lunch deduction if the total minutes meet or exceed the lunch threshold.
        if (totalMinutes >= shift.getTotalMinutes()) {
            totalMinutes -= shift.getLunchMinutes();
        }
        
        return totalMinutes;
    }
}
