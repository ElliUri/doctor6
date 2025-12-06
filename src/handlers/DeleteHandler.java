package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.PatientDataModel;
import utils.Utils;

import java.io.IOException;
import java.util.Map;

public class DeleteHandler implements RouteHandler {

    private final PatientDataModel patients;

    public DeleteHandler(PatientDataModel patients) {
        this.patients = patients;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = query == null ? Map.of() : Utils.parsedUrlEncoded(query, "&");

        String idStr = params.get("id");
        String date = params.get("date");

        if (idStr == null || date == null) {
            exchange.getResponseHeaders().add("Location", "/");
            exchange.sendResponseHeaders(302, -1);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            patients.deletePatient(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        exchange.getResponseHeaders().add("Location", "/day?date=" + date);
        exchange.sendResponseHeaders(302, -1);
    }
}
