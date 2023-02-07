package webserver.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Headers {

    private final Map<String, String> headers = new HashMap<>();

    public void add(String header) {
        String[] headerSplit = header.split(":");
        String key = headerSplit[0];
        String value = headerSplit[1].trim();
        headers.put(key, value);
    }

    public String get(String key) {
        return headers.get(key);
    }

    public int getContentLength() {
        String contentLength = headers.get("Content-Length");
        if (Objects.isNull(contentLength)) {
            return 0;
        }

        return Integer.parseInt(contentLength);
    }
}
