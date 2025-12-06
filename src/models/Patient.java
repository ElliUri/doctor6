package models;

public class Patient {
    private String fullName;
    private String birthDate;
    private PatientType type;
    private String anamnesis;
    private String phone;
    private String address;

    public Patient(String fullName, String birthDate, PatientType type, String anamnesis, String phone, String address) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.type = type;
        this.anamnesis = anamnesis;
        this.phone = phone;
        this.address = address;
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
