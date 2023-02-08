package webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private final RequestHeader header;
    private final String body;

    public Request(RequestHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> convertBodyToMap() {
        Map<String, String> MappedBody = new HashMap<>();
        Arrays.stream(body.split("&"))
                .forEach(v -> {
                    String[] kv = v.split("=");
                    MappedBody.put(kv[0], kv[1]);
                });

        return MappedBody;
    }
}
