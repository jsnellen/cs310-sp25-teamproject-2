package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalTime;

public class DailySchedule {

    // Instance fields corresponding to the columns in the "dailyschedule" table
    private LocalTime shiftStart;
    private LocalTime shiftStop;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    private int interval;                 // in minutes
    private int gracePeriod;              // in minutes
    private int dockPenalty;              // in minutes
    private int lunchDeductionThreshold;  // in minutes

    // Constructor to initialize all fields
    public DailySchedule(LocalTime shiftStart, LocalTime shiftStop,
                         LocalTime lunchStart, LocalTime lunchStop,
                         int interval, int gracePeriod, int dockPenalty,
                         int lunchDeductionThreshold) {
        this.shiftStart = shiftStart;
        this.shiftStop = shiftStop;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dockPenalty = dockPenalty;
        this.lunchDeductionThreshold = lunchDeductionThreshold;
    }

    // Getters and Setters

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(LocalTime shiftStart) {
        this.shiftStart = shiftStart;
    }

    public LocalTime getShiftStop() {
        return shiftStop;
    }

    public void setShiftStop(LocalTime shiftStop) {
        this.shiftStop = shiftStop;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(LocalTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }

    public void setLunchStop(LocalTime lunchStop) {
        this.lunchStop = lunchStop;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getDockPenalty() {
        return dockPenalty;
    }

    public void setDockPenalty(int dockPenalty) {
        this.dockPenalty = dockPenalty;
    }

    public int getLunchDeductionThreshold() {
        return lunchDeductionThreshold;
    }

    public void setLunchDeductionThreshold(int lunchDeductionThreshold) {
        this.lunchDeductionThreshold = lunchDeductionThreshold;
    }

    @Override
    public String toString() {
        return "DailySchedule{" +
                "shiftStart=" + shiftStart +
                ", shiftStop=" + shiftStop +
                ", lunchStart=" + lunchStart +
                ", lunchStop=" + lunchStop +
                ", interval=" + interval +
                ", gracePeriod=" + gracePeriod +
                ", dockPenalty=" + dockPenalty +
                ", lunchDeductionThreshold=" + lunchDeductionThreshold +
                '}';
    }
}
