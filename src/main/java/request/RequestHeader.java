package request;

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

    public String getUri() {
        return header.get("URI");
    }

    public String getMethod() {
        return header.get("method");
    }

    public String parseGetParams() {
        String uri = header.get("URI");

        if (uri.contains("?")) {
            String[] split = uri.split("\\?");
            header.put("URI", split[0]);
            return split[1];
        }
        return null;
    }
}
