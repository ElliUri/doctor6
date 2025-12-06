package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.Patient;
import models.PatientDataModel;
import models.PatientType;
import server.DoctorServer;
import utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AddHandler implements RouteHandler {

    private final PatientDataModel patients;

    public AddHandler(PatientDataModel patients) {
        this.patients = patients;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            handleGet(exchange);
        } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            handlePost(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = query == null ? Map.of() : Utils.parsedUrlEncoded(query, "&");

        LocalDate date;
        try {
            date = LocalDate.parse(params.getOrDefault("date", LocalDate.now().toString()));
        } catch (Exception e) {
            date = LocalDate.now();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("date", date.toString());
        model.put("day", date.getDayOfMonth());
        model.put("month", date.getMonthValue());
        model.put("year", date.getYear());

        model.put("errorTime", false);
        model.put("errorFullName", false);
        model.put("errorBirthDate", false);
        model.put("errorType", false);

        model.put("type", "");


        DoctorServer.renderTemplate(exchange, "add.html", model);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = Utils.getRequestBody(exchange);
        Map<String, String> form = Utils.parsedUrlEncoded(body, "&");

        String dateStr = form.get("date");
        String time = form.get("time");
        String fullName = form.get("fullName");
        String birthDate = form.get("birthDate");
        String anamnesis = form.get("anamnesis");
        String typeStr = form.get("type");

        LocalDate appointmentDate;

        try {
            appointmentDate = LocalDate.parse(dateStr);
        } catch (Exception e) {
            appointmentDate = LocalDate.now();
        }

        Map<String, Object> model = new HashMap<>();
        boolean hasErrors = false;

        model.put("date", appointmentDate.toString());
        model.put("time", time);
        model.put("fullName", fullName);
        model.put("birthDate", birthDate);
        model.put("anamnesis", anamnesis);
        model.put("type", typeStr);

        model.put("errorTime", false);
        model.put("errorFullName", false);
        model.put("errorBirthDate", false);
        model.put("errorType", false);


        if (time == null || time.isBlank()) {
            model.put("errorTime", true);
            hasErrors = true;
        }
        if (fullName == null || fullName.isBlank()) {
            model.put("errorFullName", true);
            hasErrors = true;
        }
        if (birthDate == null || birthDate.isBlank()) {
            model.put("errorBirthDate", true);
            hasErrors = true;
        }
        if (typeStr == null || typeStr.isBlank()) {
            model.put("errorType", true);
            hasErrors = true;
        }

        if (hasErrors) {
            DoctorServer.renderTemplate(exchange, "add.html", model);
            return;
        }

        PatientType type = PatientType.valueOf(typeStr);
        int id = patients.getAll().stream().mapToInt(Patient::getId).max().orElse(0) + 1;

        Patient newPatient = new Patient(
                id,
                time == null ? "00:00" : time,
                fullName == null ? "" : fullName,
                birthDate == null ? "" : birthDate,
                type,
                anamnesis == null ? "" : anamnesis,
                "",
                "",
                appointmentDate.toString()
        );


        patients.addPatient(newPatient);
        patients.saveUsers();

        Map<String, Object> modelDay = Map.of(
                "date", appointmentDate.toString(),
                "day", appointmentDate.getDayOfMonth(),
                "month", appointmentDate.getMonthValue(),
                "year", appointmentDate.getYear(),
                "appointments", patients.getAllForDate(appointmentDate)
        );

        DoctorServer.renderTemplate(exchange, "day.html", modelDay);
    }
}
