package http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestBody {
    private final String body;

    public RequestBody(String body) {
        this.body = body;
    }

    public RequestParam getRequestParam() {
        return new RequestParam(URLDecoder.decode(body, StandardCharsets.UTF_8));
    }
}
