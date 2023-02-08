package http;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeader {
    private static final String DELIMITER = ": ";
    private static final int SPLIT_LIMIT = 2;
    private static final int INDEX_OF_KEY = 0;
    private static final int INDEX_OF_VALUE = 1;
    public static final String GET_COOKIE = "Cookie";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String CONTENT_LENGTH = "Content-Length";

    private final Map<String, String> headers = new HashMap<>();

    public HttpHeader() {
    }

    public HttpHeader(BufferedReader reader) {
        String line;
        try {
            while (!(line = reader.readLine()).isBlank()) {
                String[] keyValue = line.split(DELIMITER, SPLIT_LIMIT);
                headers.put(keyValue[INDEX_OF_KEY], keyValue[INDEX_OF_VALUE]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    public void putHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void setCookie(String value) {
        if (headers.containsKey(SET_COOKIE)) {
            headers.replace(SET_COOKIE, value);
            return;
        }
        headers.put(SET_COOKIE, value);
    }

    public void setContentLength(int value) {
        if (headers.containsKey(CONTENT_LENGTH)) {
            headers.replace(CONTENT_LENGTH, Integer.toString(value));
            return;
        }
        headers.put(CONTENT_LENGTH, Integer.toString(value));
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getCookie() {
        // TODO: Cookie 객체 생성
        return headers.getOrDefault(GET_COOKIE, "");
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }

    public String toString() {
        return headers.entrySet().stream().map(e -> e.getKey() + DELIMITER + e.getValue() + "\r\n").collect(Collectors.joining());
    }
}
