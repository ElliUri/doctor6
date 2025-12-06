package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.PatientDataModel;
import server.DoctorServer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class HomeBooksHandler implements RouteHandler {
    private final PatientDataModel patients;

    public HomeBooksHandler(PatientDataModel patients) {
        this.patients = patients;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, Object> model = new HashMap<>();

        LocalDate today = LocalDate.now();
        model.put("today", today.getDayOfMonth());
        model.put("month", today.getMonthValue());
        model.put("year", today.getYear());

        List<Map<String, Object>> days = new ArrayList<>();

        for (int day = today.getDayOfMonth(); day <= today.lengthOfMonth(); day++) {
            LocalDate d = LocalDate.of(today.getYear(), today.getMonth(), day);

            Map<String, Object> dayModel = new HashMap<>();
            dayModel.put("day", day);
            dayModel.put("date", d);
            dayModel.put("appointments", patients.getAppointmentsForDate(d));

            dayModel.put("isToday", day == today.getDayOfMonth());

            dayModel.put("canBook", !d.isBefore(today));

            days.add(dayModel);
        }

        model.put("days", days);
        DoctorServer.renderTemplate(exchange, "month.html", model);
    }
}