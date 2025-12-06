package handlers;

import com.sun.net.httpserver.HttpExchange;
import server.DoctorServer;

import java.io.IOException;
import java.util.Map;

public class ErrorHandler implements RouteHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DoctorServer.renderTemplate(exchange, "error.html",
                Map.of("message", "Error при обработке запроса"));
    }
}
