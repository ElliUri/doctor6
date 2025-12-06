package models;

public class Appointment implements Comparable<Appointment> {
    private final String time;
    private final Patient patient;

    public Appointment(String time, Patient patient) {
        this.time = (time == null || time.isBlank()) ? "00:00" : time;
        this.patient = patient;
    }

    public String getTime() {
        return time;
    }

    public Patient getPatient() {
        return patient;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.time.compareTo(o.time);
    }
}