package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestHeader {

    Map<String, String> header = new HashMap<>();

    public RequestHeader(String data) {
        String[] arr = data.split("\n");
        String[] firstLine = arr[0].split(" ");

        header.put("method", firstLine[0]);
        header.put("URI", firstLine[1]);
        header.put("version", firstLine[2]);

        for (int i = 1; i < arr.length; i++) {
            String[] temp = arr[i].split(":", 2);
            header.put(temp[0], temp[1].trim());
        }
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(header.get(key));
    }
}
