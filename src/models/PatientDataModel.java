package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PatientDataModel {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final Path path = Path.of("data/json/patient.json");
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private List<Patient> patients = new ArrayList<>();

    public PatientDataModel() {
        loadPatiens();
    }

    private void loadPatiens() {
        try {
            if (!Files.exists(path)) {
                patients = new ArrayList<>();
                return;
            }

            String json = Files.readString(path, StandardCharsets.UTF_8);

            Type listType = new TypeToken<List<Patient>>() {}.getType();
            patients = gson.fromJson(json, listType);

            if (patients == null) {
                patients = new ArrayList<>();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {

            gson.toJson(patients, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addPatient(Patient patient) {
        return patients.add(patient);
    }

    public Patient getUserById(int id) {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Patient> getAll() {
        return patients;
    }


    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        List<Appointment> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getAppointmentDate() != null &&
                    LocalDate.parse(p.getAppointmentDate(), dateFormatter).equals(date)) {
                result.add(new Appointment(p.getAppointmentTime(), p));
            }
        }

        result.sort(null);
        return result;
    }


    public List<Patient> getAllForDate(LocalDate date) {
        List<Patient> result = new ArrayList<>();

        for (Patient p : patients) {
            if (p.getAppointmentDate() != null) {
                try {
                    LocalDate patientDate = LocalDate.parse(p.getAppointmentDate(), dateFormatter);
                    if (patientDate.equals(date)) {
                        result.add(p);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        result.sort((p1, p2) -> {
            String t1 = p1.getAppointmentTime() != null ? p1.getAppointmentTime() : "00:00";
            String t2 = p2.getAppointmentTime() != null ? p2.getAppointmentTime() : "00:00";
            return t1.compareTo(t2);
        });

        return result;
    }


    public boolean deletePatient(int id) {
        Patient target = patients.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        if (target == null) {
            return false;
        }

        boolean removed = patients.remove(target);

        if (removed) {
            saveUsers();
        }

        return removed;
    }

}
