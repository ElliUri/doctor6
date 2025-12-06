package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaySchedule {

    private int day;
    private List<Appointment> appointments = new ArrayList<>();

    public DaySchedule(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public List<Appointment> getAppointments() {
        Collections.sort(appointments);
        return appointments;
    }

    public void addAppointment(Appointment app) {
        appointments.add(app);
    }

    public void removeAppointment(String time) {
        appointments.removeIf(a -> a.getTime().equals(time));
    }
}
