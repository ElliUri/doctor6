package handlers;

import com.sun.net.httpserver.HttpExchange;
import models.AppointmentDataModel;
import models.PatientDataModel;

import java.io.IOException;

public class HomeBooksHandler implements RouteHandler{
    public HomeBooksHandler(AppointmentDataModel appointments, PatientDataModel patients) {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
    }
}
