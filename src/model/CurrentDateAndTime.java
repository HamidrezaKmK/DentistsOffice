package model;

public class CurrentDateAndTime {

    private static CurrentDateAndTime singleInstance = null;

    private String current_date;
    private String current_time;
    private String current_week_day;

    public static CurrentDateAndTime getInstance() {
        if (singleInstance == null)
            singleInstance = new CurrentDateAndTime();
        return singleInstance;
    }

    public void clear() {
        this.current_date = null;
        this.current_time = null;
        this.current_week_day = null;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getCurrent_week_day() {
        return current_week_day;
    }

    public void setCurrent_week_day(String current_week_day) {
        this.current_week_day = current_week_day;
    }
}
