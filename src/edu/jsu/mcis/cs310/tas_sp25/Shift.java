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

public class Shift {
    private int id;
    private Badge badge;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Shift(int id, Badge badge, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.badge = badge;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public Badge getBadge() {
        return badge;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Shift{id=" + id + ", badge=" + badge + ", startTime=" + startTime + ", endTime=" + endTime + "}";
    }
}
