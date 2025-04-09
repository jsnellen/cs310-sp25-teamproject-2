package edu.jsu.mcis.cs310.tas_sp25;

/**
 *
 * @author Dillon
 */
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import edu.jsu.mcis.cs310.tas_sp25.Shift;
import java.time.LocalTime;

public class Punch {

    private Integer id;
    private Integer terminalid;
    private Badge badge;
    private EventType punchType;
    private LocalDateTime originalTimestamp;
    private PunchAdjustmentType adjustmenttype;
    private LocalDateTime adjustedTimestamp;
    private Shift shift;
    private Shift start;
    private Shift end;
    private LocalTime shiftStart;

    /**
     * Constructs a new Punch object with specified terminal ID, badge, and punch
     * type, automatically setting the original timestamp to the current time and
     * leaving the ID and adjusted timestamp as null.
     *
     * @param terminalid The ID of the terminal where the punch event occurred.
     * @param badge      The Badge object associated with the employee who made the
     *                   punch.
     * @param punchType  The type of punch event (CLOCK_IN, CLOCK_OUT, or TIME_OUT).
     * 
     */
    public Punch(Integer terminalid, Badge badge, EventType punchType) {
        this.id = null;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchType = punchType;
        this.originalTimestamp = LocalDateTime.now();
        this.adjustmenttype = null;
    }

    /**
     * Constructs a Punch object with specified ID, terminal ID, badge, original
     * timestamp, and punch type.
     *
     * @param id                The ID of the punch event.
     * @param terminalid        The ID of the terminal where the punch event
     *                          occurred.
     * @param badge             The Badge object associated with the employee who
     *                          made the punch.
     * @param originalTimestamp The original timestamp of the punch event.
     * @param punchType         The type of punch event (CLOCK_IN, CLOCK_OUT, or
     *                          TIME_OUT).
     */
    public Punch(Integer id, Integer terminalid, Badge badge, LocalDateTime originalTimestamp, EventType punchType) {
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.originalTimestamp = originalTimestamp;
        this.punchType = punchType;
    }

    /**
     * This method adjusts the timestamp of a punch event based on a given shift,
     * taking into account various factors such as round intervals, grace periods,
     * and dock penalties.
     *
     * @param shift The Shift object containing shift details used for adjustment.
     */
    public void adjust(Shift shift) {

        adjustmenttype = null;
        adjustedTimestamp = null;
        boolean isWeekend = false;
        DayOfWeek day = originalTimestamp.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            isWeekend = true;
        }

        int interval = shift.getRoundInterval();
        int grace = shift.getGracePeriod();
        int dock = shift.getDockPenalty();

        LocalTime shiftStart = shift.getShiftStart();
        LocalTime shiftStop = shift.getShiftStop();
        LocalTime lunchStart = shift.getLunchStart();
        LocalTime lunchStop = shift.getLunchStop();

        LocalDateTime shiftStartDateTime = originalTimestamp.with(shiftStart);
        LocalDateTime shiftStopDateTime = originalTimestamp.with(shiftStop);
        LocalDateTime lunchStartDateTime = originalTimestamp.with(lunchStart);
        LocalDateTime lunchStopDateTime = originalTimestamp.with(lunchStop);

        LocalDateTime shiftStartInterval = shiftStartDateTime.minusMinutes(interval);
        LocalDateTime shiftStartGrace = shiftStartDateTime.plusMinutes(grace);
        LocalDateTime shiftStartDock = shiftStartDateTime.plusMinutes(dock);

        LocalDateTime shiftStopInterval = shiftStopDateTime.plusMinutes(interval);
        LocalDateTime shiftStopGrace = shiftStopDateTime.minusMinutes(grace);
        LocalDateTime shiftStopDock = shiftStopDateTime.minusMinutes(dock);

        if (punchType == EventType.CLOCK_IN) {
            if (originalTimestamp.isAfter(shiftStartInterval.minusSeconds(1))
                    && originalTimestamp.isBefore(shiftStartDateTime)) {
                adjustedTimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
            } else if (originalTimestamp.isAfter(shiftStartDateTime) && originalTimestamp.isBefore(shiftStartGrace)) {
                adjustedTimestamp = shiftStartDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_START;
            } else if (originalTimestamp.isAfter(shiftStartGrace)
                    && originalTimestamp.isBefore(shiftStartDock.plusSeconds(1))) {
                adjustedTimestamp = shiftStartDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
            } else if (!isWeekend && originalTimestamp.isAfter(lunchStartDateTime)
                    && originalTimestamp.isBefore(lunchStopDateTime)) {
                adjustedTimestamp = lunchStopDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_STOP;
            }
        } else if (punchType == EventType.CLOCK_OUT || punchType == EventType.TIME_OUT) {
            if (originalTimestamp.isAfter(shiftStopDateTime)
                    && originalTimestamp.isBefore(shiftStopInterval.plusSeconds(1))) {
                adjustedTimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
            } else if (originalTimestamp.isBefore(shiftStopDateTime) && originalTimestamp.isAfter(shiftStopGrace)) {
                adjustedTimestamp = shiftStopDateTime;
                adjustmenttype = PunchAdjustmentType.SHIFT_STOP;
            } else if (originalTimestamp.isBefore(shiftStopGrace)
                    && originalTimestamp.isAfter(shiftStopDock.minusSeconds(1))) {
                adjustedTimestamp = shiftStopDock;
                adjustmenttype = PunchAdjustmentType.SHIFT_DOCK;
            } else if (!isWeekend && originalTimestamp.isAfter(lunchStartDateTime)
                    && originalTimestamp.isBefore(lunchStopDateTime)) {
                adjustedTimestamp = lunchStartDateTime;
                adjustmenttype = PunchAdjustmentType.LUNCH_START;
            }
        }

        if (adjustmenttype == null) {
            int adjustMinute;
            int minutes = originalTimestamp.getMinute();

            if ((minutes % interval) < (interval / 2)) {
                adjustMinute = (Math.round(minutes / interval) * interval);
            } else {
                adjustMinute = (Math.round(minutes / interval) * interval) + interval;
            }

            if ((adjustMinute / 60) == 1) {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedTimestamp = originalTimestamp.withHour(originalTimestamp.getHour() + 1).withMinute(0)
                        .withSecond(0).withNano(0);
            } else {
                adjustmenttype = PunchAdjustmentType.INTERVAL_ROUND;
                adjustedTimestamp = originalTimestamp.withMinute(adjustMinute).withSecond(0).withNano(0);
            }
            if ((originalTimestamp.getMinute() == adjustedTimestamp.getMinute())
                    && (originalTimestamp.getHour() == adjustedTimestamp.getHour())) {
                adjustedTimestamp = originalTimestamp.withSecond(0).withNano(0);
                adjustmenttype = PunchAdjustmentType.NONE;
            }

        }

    }

    // Getter methods
    /**
     * Gets the ID of the punch event.
     *
     * @return The ID of the punch event.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the ID of the terminal where the punch event was made.
     * 
     * @return the ID of the terminal.
     */
    public Integer getTerminalid() {
        return terminalid;
    }

    /**
     * Gets the Badge object associated with the employee who made the punch.
     *
     * 
     * @return The Badge object.
     */
    public Badge getBadge() {
        return badge;
    }

    /**
     * Gets the event type determining if its clock in, clock out, or time out.
     * 
     * @return The Event type.
     */
    public EventType getPunchtype() {
        return punchType;
    }

    /**
     * Gets the original timestamp of the punch event.
     * 
     * @return The original timestamp of the punch.
     */
    public LocalDateTime getOriginaltimestamp() {
        return originalTimestamp;
    }

    /**
     * Gets the adjustment type of the punch.
     * 
     * @return The Punch Adjustment Type.
     */
    public PunchAdjustmentType getAdjustmentType() {
        return adjustmenttype;
    }

    /**
     * Gets the adjusted timestamp of the punch.
     * 
     * @return The adjusted timestamp of the punch.
     */
    public LocalDateTime getAdjustedTimestamp() {
        return adjustedTimestamp;
    }

    /**
     * Prints the original timestamp of the punch in a formatted string.
     * 
     * @return The original timestamp with the badge, punchType, and day of the
     *         week.
     */
    public String printOriginal() {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        DayOfWeek dayOfTheWeek = originalTimestamp.getDayOfWeek();

        StringBuilder build = new StringBuilder();

        build.append("#").append(badge.getId())
                .append(" ")
                .append(punchType).append(": ")
                .append(dayOfTheWeek.name().substring(0, 3))
                .append(" ")
                .append(originalTimestamp.format(format));

        return build.toString();
    }

    /**
     * Prints the adjusted timestamp of the punch in a formatted string.
     * 
     * @return The adjusted timestamp with the badge, punchType, day of the week,
     *         and adjustment type.
     */
    public String printAdjusted() {
        StringBuilder build = new StringBuilder();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        DayOfWeek dayOfTheWeek = originalTimestamp.getDayOfWeek();

        build.append("#")
                .append(badge.getId()).append(" ")
                .append(punchType).append(": ")
                .append(dayOfTheWeek.name().substring(0, 3))
                .append(" ")
                .append(adjustedTimestamp.format(format))
                .append(" (").append(adjustmenttype).append(")");

        return build.toString();

    }

    @Override
    public String toString() {
        return printOriginal();
    }
}