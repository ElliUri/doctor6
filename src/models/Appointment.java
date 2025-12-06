package models;

public class Appointment implements Comparable<Appointment> {
    private String time;
    private Patient patient;

    public Appointment(String time, Patient patient) {
        this.time = time;
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
