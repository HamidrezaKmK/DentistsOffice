package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeInterval implements Comparable<TimeInterval> {
    public LocalDate beginDate, endDate;
    public LocalTime beginTime, endTime;
    public String description;

    public TimeInterval(LocalTime beginTime, LocalTime endTime) {
        this.beginDate = LocalDate.parse("0000-00-00");
        this.endDate = LocalDate.parse("0000-00-00");
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.description = "";
    }

    public TimeInterval(LocalDate beginDate, LocalDate endDate, LocalTime beginTime, LocalTime endTime, String description) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.description = description;
    }

    @Override
    public int compareTo(TimeInterval interval) {
        if (!this.beginDate.equals(interval.beginDate))
            return this.beginDate.compareTo(interval.beginDate);
        return this.beginTime.compareTo(interval.beginTime);
    }
}
