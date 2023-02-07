package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestHeader {

    Map<String, String> header = new HashMap<>();

    public RequestHeader(String data) {
        String[] arr = data.split("\n");
        String[] requestLine = arr[0].split(" ");

        header.put("method", requestLine[0]);
        header.put("URI", requestLine[1]);
        header.put("version", requestLine[2]);

        for (int i = 1; i < arr.length; i++) {
            String[] line = arr[i].split(":", 2);
            header.put(line[0], line[1].trim());
        }
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(header.get(key));
    }
}
