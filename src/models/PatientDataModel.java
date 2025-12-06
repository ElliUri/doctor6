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
import java.util.ArrayList;
import java.util.List;

public class PatientDataModel {

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
        return true;
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
}
