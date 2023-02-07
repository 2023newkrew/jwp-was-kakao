package infra.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class Headers {
    public static String DELIMITER = ": ";
    public static String CONTENT_TYPE = "Content-Type";
    public static String CONTENT_LENGTH = "Content-Length";
    public static String LOCATION = "Location";

    private Map<String, String> headers;

    public Headers() {
        this.headers = new LinkedHashMap<>();
    }

    public void put(String key, String value) {
        this.headers.put(key, value);
    }

    public String get(String key) {
        return this.headers.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.headers.forEach((key, value) -> {
            sb.append(key);
            sb.append(Headers.DELIMITER);
            sb.append(value);
            sb.append(HttpMessageBase.LINE_DELIMITER);
        });
        return sb.toString();
    }
}
