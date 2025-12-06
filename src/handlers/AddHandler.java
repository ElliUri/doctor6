package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.Patient;
import models.PatientDataModel;
import models.PatientType;
import server.DoctorServer;
import utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
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
            if (params.containsKey("date")) {
                date = LocalDate.parse(params.get("date"));
            } else {
                int day = Integer.parseInt(params.getOrDefault("d", String.valueOf(LocalDate.now().getDayOfMonth())));
                int month = Integer.parseInt(params.getOrDefault("m", String.valueOf(LocalDate.now().getMonthValue())));
                int year = Integer.parseInt(params.getOrDefault("y", String.valueOf(LocalDate.now().getYear())));
                date = LocalDate.of(year, month, day);
            }
        } catch (Exception e) {
            date = LocalDate.now();
        }

        Map<String, Object> model = new HashMap<>();
        model.put("day", date.getDayOfMonth());
        model.put("month", date.getMonthValue());
        model.put("year", date.getYear());
        model.put("date", date.toString());

        DoctorServer.renderTemplate(exchange, "add.html", model);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = Utils.getRequestBody(exchange);
        Map<String, String> form = Utils.parsedUrlEncoded(body, "&");

        try {
            String dateStr = form.get("day");
            String time = form.get("time");
            String fullName = form.get("fullName");
            String birthDate = form.get("birthDate");
            PatientType type = PatientType.valueOf(form.get("type"));
            String anamnesis = form.get("anamnesis");

            LocalDate appointmentDate = LocalDate.parse(dateStr);

            if (appointmentDate.isBefore(LocalDate.now())) {
                DoctorServer.renderTemplate(exchange, "error.html",
                        Map.of("message", "Нельзя добавить пациента на прошедшую дату"));
                return;
            }

            int id = patients.getAll().stream().mapToInt(Patient::getId).max().orElse(0) + 1;

            Patient newPatient = new Patient(
                    id,
                    time,
                    fullName,
                    birthDate,
                    type,
                    anamnesis,
                    "",
                    "",
                    appointmentDate.toString()
            );

            patients.addPatient(newPatient);
            patients.saveUsers();

            exchange.getResponseHeaders().add("Location", "/day?date=" + appointmentDate);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);

        } catch (Exception e) {
            DoctorServer.renderTemplate(exchange, "error.html",
                    Map.of("message", "Ошибка при добавлении пациента: " + e.getMessage()));
        }
    }
}
