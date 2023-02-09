package webserver.http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestBody {

    private String body;


    public RequestBody(String body) {
        this.body = URLDecoder.decode(body, StandardCharsets.UTF_8);
    }

    public String getBody() {
        return body;
    }

    public boolean isEmpty() {
        return body == null || body.length() == 0;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "body='" + body + '\'' +
                '}';
    }
}
