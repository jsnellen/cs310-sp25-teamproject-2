package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.util.ArrayList;
import java.util.HashMap;
import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_sp25.Punch;  // Adjust the package name if necessary

public class DAOUtility {

    // Other methods and constants in DAOUtility ...

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
        // Create an ArrayList to hold the data for each punch as a HashMap
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        // Iterate over each Punch object in the provided list
        for (Punch p : dailypunchlist) {
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", String.valueOf(p.getId()));
            punchData.put("badgeid", String.valueOf(p.getBadge()));
            punchData.put("terminalid", String.valueOf(p.getTerminalid()));
            punchData.put("punchtype", p.getPunchtype().toString());
            punchData.put("adjustmenttype", p.getAdjustmentType().toString());
            punchData.put("originaltimestamp", p.printOriginal());
            punchData.put("adjustedtimestamp", p.printAdjusted());
            
            // Add the map to the list, preserving the punch order
            jsonData.add(punchData);
        }
        
        // Convert the entire list to a JSON string and return it
        return Jsoner.serialize(jsonData);
    }
}
