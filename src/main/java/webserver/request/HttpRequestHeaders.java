package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaders {

    public static final String HEADER_DELIMITER = ":";
    private Map<String, String> headers;

    public HttpRequestHeaders(BufferedReader reader) throws IOException {
        parseHeaders(reader);
    }

    private void parseHeaders(BufferedReader reader) throws IOException {
        headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int colonIndex = line.indexOf(HEADER_DELIMITER);
            String headerKey = line.substring(0, colonIndex)
                    .trim();
            String headerValue = line.substring(colonIndex + 1)
                    .trim();
            headers.put(headerKey, headerValue);
        }
    }

    public boolean containsKey(String key) {
        return headers.containsKey(key);
    }

    public String get(String key) {
        return headers.get(key);
    }
}
