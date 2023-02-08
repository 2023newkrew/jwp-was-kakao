package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class Headers {

    private final Map<String, String> headers = new HashMap<>();

    public void put(String header) {
        String[] headerSplit = header.split(":");
        String key = headerSplit[0];
        String value = headerSplit[1].trim();
        headers.put(key, value);
    }

    public String get(String key) {
        return headers.get(key);
    }

    public byte[] getBytes() {
        return headers.keySet()
                .stream()
                .map(key -> key + ": " + headers.get(key) + "\r\n")
                .reduce(String::concat)
                .orElseThrow(RuntimeException::new)
                .concat("\r\n")
                .getBytes();
    }
}
