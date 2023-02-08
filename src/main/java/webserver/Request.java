package webserver;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
        return Arrays.stream(body.split("&"))
                .map(v -> v.split("="))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }
}
