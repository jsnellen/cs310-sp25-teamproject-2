package edu.jsu.mcis.cs310.tas_sp25;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Absenteeism {

    private final Employee employee;
    private final LocalDate localDate;
    private final BigDecimal bigDecimal;

    public Absenteeism(Employee employee, LocalDate localDate, BigDecimal bigDecimal) {
        this.employee = employee;
        this.localDate = localDate;
        this.bigDecimal = bigDecimal;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        
        // Build the string in the format: "#<badge> (Pay Period Starting <MM-dd-yyyy>): <percentage%"
        s.append('#').append(employee.getBadge()).append(" ");  // Add employee badge
        s.append("(Pay Period Starting ").append(localDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))).append("): ");  // Add formatted pay period date
        
        // Format absenteeism rate as percentage with two decimals (rounded)
        BigDecimal percentage = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        s.append(percentage.toString()).append("%");

        return s.toString();
    }

    public static BigDecimal calculateAbsenteeismRate(Shift shift, LocalDate startDate, LocalDate endDate, Duration actualWorkedTime) {
        Duration scheduledTime = Duration.between(shift.getShiftStart(), shift.getShiftStop());
        Duration missedTime = scheduledTime.minus(actualWorkedTime);

        return new BigDecimal(missedTime.toMinutes())
            .divide(new BigDecimal(scheduledTime.toMinutes()), 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));  // Return absenteeism as percentage
    }
}

