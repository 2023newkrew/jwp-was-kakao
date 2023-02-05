package webserver.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpRequestBody {

    private final String content;

    public HttpRequestBody(String content) {
        this.content = URLDecoder.decode(content, StandardCharsets.UTF_8);
    }

    public String getContent() {
        return content;
    }
}
