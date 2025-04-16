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

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchlist, Shift s) {
        long totalAccruedMinutes = 0;
        long scheduledMinutes = 0;

        // Group punches by LocalDate
        TreeMap<LocalDate, ArrayList<Punch>> dailyPunches = new TreeMap<>();
        for (Punch punch : punchlist) {
            LocalDate date = punch.getOriginaltimestamp().toLocalDate();
            dailyPunches.putIfAbsent(date, new ArrayList<>());
            dailyPunches.get(date).add(punch);
        }

        for (Map.Entry<LocalDate, ArrayList<Punch>> entry : dailyPunches.entrySet()) {
            ArrayList<Punch> dailyList = entry.getValue();
            dailyList.sort(Comparator.comparing(Punch::getAdjustedTimestamp));

            for (int i = 0; i < dailyList.size() - 1; i += 2) {
                Punch inPunch = dailyList.get(i);
                Punch outPunch = dailyList.get(i + 1);

                long minutesWorked = Duration.between(inPunch.getAdjustedTimestamp(), outPunch.getAdjustedTimestamp()).toMinutes();

                // Apply lunch deduction if over threshold
                if (minutesWorked >= s.getLunchThreshold()) {
                    minutesWorked -= s.getLunchDuration().toMinutes();
                }

                totalAccruedMinutes += minutesWorked;
            }

            // Assume any day with punches is a scheduled workday
            scheduledMinutes += s.getShiftDuration().toMinutes();
        }

        // Ensure absenteeism is not negative, set to 0 if total accrued minutes exceed scheduled minutes
        if (totalAccruedMinutes > scheduledMinutes) {
            totalAccruedMinutes = scheduledMinutes;
        }

        BigDecimal absenteeism;
        if (scheduledMinutes == 0) {
            absenteeism = BigDecimal.ZERO;
        } else {
            absenteeism = BigDecimal.valueOf(100)
                .multiply(BigDecimal.valueOf(scheduledMinutes - totalAccruedMinutes))
                .divide(BigDecimal.valueOf(scheduledMinutes), 2, RoundingMode.HALF_UP);
        }

        return absenteeism;
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        ArrayList<Map<String, Object>> punchDataList = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");

        for (Punch punch : punchlist) {
            Map<String, Object> punchData = new LinkedHashMap<>();
            punchData.put("id", punch.getId()); // as int
            punchData.put("badgeid", punch.getBadge().getId()); // as String
            punchData.put("terminal", punch.getTerminalid()); // as int
            punchData.put("punchtype", punch.getPunchtype().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            punchData.put("originaltimestamp", punch.getOriginaltimestamp().format(format).toUpperCase());
            punchData.put("adjustedtimestamp", punch.getAdjustedTimestamp().format(format).toUpperCase());
            punchDataList.add(punchData);
        }

        int totalMinutes = calculateTotalMinutes(punchlist, shift);
        BigDecimal absenteeism = calculateAbsenteeism(punchlist, shift);

        Map<String, Object> resultJSON = new LinkedHashMap<>();
        resultJSON.put("punchlist", punchDataList);
        resultJSON.put("totalminutes", totalMinutes); // keep as integer
        resultJSON.put("absenteeism", absenteeism.setScale(2, RoundingMode.HALF_UP).toString() + "%"); // formatted as percentage

        return Jsoner.serialize(resultJSON);
    }
}


