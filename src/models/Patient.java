package models;

public class Patient {
    private int id;
    private String appointmentTime;
    private String fullName;
    private String birthDate;
    private PatientType type;
    private String anamnesis;
    private String phone;
    private String address;
    private String appointmentDate;

    public Patient(int id, String appointmentTime, String fullName, String birthDate, PatientType type, String anamnesis, String phone, String address, String appointmentDate) {
        this.id = id;
        this.appointmentTime = appointmentTime;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.type = type;
        this.anamnesis = anamnesis;
        this.phone = phone;
        this.address = address;
        this.appointmentDate = appointmentDate;
    }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }

    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public PatientType getType() {
        return type;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
