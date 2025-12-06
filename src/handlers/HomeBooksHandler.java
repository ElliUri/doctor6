package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.Appointment;
import models.Patient;
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
        int month = today.getMonthValue();
        int year = today.getYear();
        int dayOfMonth = today.getDayOfMonth();

        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        List<Map<String, Object>> days = new ArrayList<>();
        for (int d = 1; d <= daysInMonth; d++) {
            LocalDate date = LocalDate.of(year, month, d);
            List<Appointment> appointmentsForDay = patients.getAppointmentsForDate(date);
            appointmentsForDay.sort(Comparator.naturalOrder());

            Map<String, Object> dayMap = new HashMap<>();
            dayMap.put("day", d);
            dayMap.put("appointments", appointmentsForDay);
            days.add(dayMap);
        }

        model.put("month", month);
        model.put("year", year);
        model.put("today", dayOfMonth);
        model.put("days", days);

        DoctorServer.renderTemplate(exchange, "month.html", model);
    }


}
