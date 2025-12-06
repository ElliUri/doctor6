package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthSchedule {

    private int year;
    private int month;
    private List<DaySchedule> days = new ArrayList<>();

    public MonthSchedule(int year, int month) {
        this.year = year;
        this.month = month;

        LocalDate start = LocalDate.of(year, month, 1);
        int length = start.lengthOfMonth();

        for (int d = 1; d <= length; d++) {
            days.add(new DaySchedule(d));
        }
    }

    public List<DaySchedule> getDays() {
        return days;
    }

    public DaySchedule getDay(int day) {
        return days.stream().filter(d -> d.getDay() == day).findFirst().orElse(null);
    }
}
