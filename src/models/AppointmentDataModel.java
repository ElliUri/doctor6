package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Path;

public class AppointmentDataModel {

    private final Path path = Path.of("data/json/appointment.json");
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public AppointmentDataModel(PatientDataModel patients) {
    }
}
