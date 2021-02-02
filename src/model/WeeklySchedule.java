package model;

public class WeeklySchedule {

    private static WeeklySchedule singleInstance = null;

    private String from_date;
    private String to_date;

    public static WeeklySchedule getInstance() {
        if (singleInstance == null)
            singleInstance = new WeeklySchedule();
        return singleInstance;
    }

    public void clear() {
        this.from_date = null;
        this.to_date = null;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
}
