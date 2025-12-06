package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.Appointment;
import models.PatientDataModel;
import server.DoctorServer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
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

        for (int day = 1; day <= today.lengthOfMonth(); day++) {
            LocalDate d = LocalDate.of(today.getYear(), today.getMonth(), day);

            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("day", day);
            dayMap.put("date", d);
            dayMap.put("appointments", patients.getAppointmentsForDate(d));

            days.add(dayMap);
        }

        model.put("days", days);
        DoctorServer.renderTemplate(exchange, "month.html", model);

    }


}
