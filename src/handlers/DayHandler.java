package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.Patient;
import models.PatientDataModel;
import server.DoctorServer;
import utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class DayHandler implements RouteHandler {

    private final PatientDataModel patients;

    public DayHandler(PatientDataModel patients) {
        this.patients = patients;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = query == null ? Map.of() : Utils.parsedUrlEncoded(query, "&");

        LocalDate date;
        try {
            date = params.containsKey("date") ? LocalDate.parse(params.get("date")) : LocalDate.now();
        } catch (Exception e) {
            DoctorServer.renderTemplate(exchange, "error.html", Map.of("message", "Некорректная дата"));
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("date", date);
        model.put("day", date.getDayOfMonth());
        model.put("month", date.getMonthValue());
        model.put("year", date.getYear());
        model.put("appointments", patients.getAppointmentsForDate(date));

        DoctorServer.renderTemplate(exchange, "day.html", model);
    }


}
