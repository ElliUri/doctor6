import server.DoctorServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new DoctorServer("localhost", 8089).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}