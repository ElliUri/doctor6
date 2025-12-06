package utils;

import com.sun.net.httpserver.HttpExchange;
import models.Patient;
import models.PatientDataModel;
import server.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    private Utils() {
    }
    public static Map<String, String > parsedUrlEncoded(String raw, String delimiter) {
        String[] parts = raw.split(delimiter);
        Stream<Map.Entry<String, String>> stream = Arrays.stream(parts)
                .map(Utils::decode)
                .filter(Optional::isPresent)
                .map(Optional::get);
        return stream.collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    private static Optional<Map.Entry<String, String>> decode(String kv) {
        if (!kv.contains("=")) {
            return Optional.empty();
        }
        String[] parts = kv.split("=");

        if (parts.length != 2) {
            return Optional.empty();
        }
        Charset utf8 = StandardCharsets.UTF_8;
        String key = URLDecoder.decode(parts[0].strip(), utf8);
        String value = URLDecoder.decode(parts[1].strip(), utf8);
        return Optional.of(Map.entry(key, value));
    }

    public static String getRequestBody(HttpExchange exchange) {
        InputStream is = exchange.getRequestBody();
        Charset charset = StandardCharsets.UTF_8;

        InputStreamReader isr = new InputStreamReader(is, charset);
        try(BufferedReader br = new BufferedReader(isr)) {
            return br.lines().collect(Collectors.joining(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Patient getAuthorizedPatient(HttpExchange exchange, PatientDataModel employeeData)  {
        String sessionId = Cookie.get(exchange, "sessionId");
        Integer employeeId = Cookie.getUserBySession(sessionId);
        if (sessionId == null || employeeId == null) return null;
        return employeeData.getUserById(employeeId);
    }

}
