package handlers;

import com.sun.net.httpserver.HttpExchange;
import server.ContentType;
import server.DoctorServer;
import server.ResponseCodes;

import java.io.IOException;


public class NotFoundHandler implements  RouteHandler{
    @Override
    public void handle(HttpExchange exchange) {
        try {
            var data = "404 Not found".getBytes();
            DoctorServer.sendByteData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
