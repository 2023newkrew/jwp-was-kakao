package http;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    public static final String COOKIE = "Cookie";
    private static final String DELIMITER = ": ";
    private static final int SPLIT_LIMIT = 2;
    private static final int INDEX_OF_KEY = 0;
    private static final int INDEX_OF_VALUE = 1;

    private final Map<String, String> headers = new HashMap<>();

    public HttpHeader() {}

    public HttpHeader(BufferedReader reader) {
        String line;
        try {
            while (!(line = reader.readLine()).isBlank()) {
                String[] keyValuePair = line.split(DELIMITER, SPLIT_LIMIT);
                headers.put(keyValuePair[INDEX_OF_KEY], keyValuePair[INDEX_OF_VALUE]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public CookieList getCookie() {
        String cookieString = headers.getOrDefault(COOKIE, "");
        return CookieList.parse(cookieString);
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }
}
