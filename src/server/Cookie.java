package server;

import com.sun.net.httpserver.HttpExchange;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Cookie<V> {
    private final String name;
    private final V value;
    private Integer maxAge;
    private boolean httpOnly;
    private String path;
    private static final Map<String, Integer> sessions = new HashMap<>();

    public Cookie(String name, V value) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(value);
        this.name = name.strip();
        this.value = value;
    }

    public static <V> Cookie make(String name, V value) {
        return new Cookie<>(name, value);
    }

    public void setMaxAge(Integer maxAgeInSeconds) {
        this.maxAge = maxAgeInSeconds;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    private V getValue() { return value; }
    private Integer getMaxAge() { return maxAge; }
    private String getName() { return name; }
    private boolean isHttpOnly() { return httpOnly; }

    public static Map<String, String> parse(String raw) {
        return Utils.parsedUrlEncoded(raw, ";");
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        Charset utf8  = StandardCharsets.UTF_8;

        String encodedName = URLEncoder.encode(getName().strip(), utf8);

        String stringValue = getValue().toString();
        String encodedValue = URLEncoder.encode(stringValue, utf8);

        sb.append(String.format("%s=%s", encodedName, encodedValue));

        if (getMaxAge() != null) {
            sb.append(String.format("; Max-Age=%s", getMaxAge()));
        }

        if (isHttpOnly()) {
            sb.append("; HttpOnly");
        }

        if (getPath() != null) {
            sb.append("; Path=").append(getPath());
        }

        return sb.toString();
    }

    public static String get(HttpExchange exchange, String name) {
        String cookie = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookie == null) return null;

        Map<String, String> cookies = Cookie.parse(cookie);
        return cookies.get(name);
    }

    public static String createSession(int userId) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userId);
        return sessionId;
    }

    public static Integer getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}