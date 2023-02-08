package webserver.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpRequestBody {

    private final String content;

    private HttpRequestBody(String content) {
        this.content = content;
    }

    public static HttpRequestBody from(String content) {
        return new HttpRequestBody(URLDecoder.decode(content, StandardCharsets.UTF_8));
    }

    public String getContent() {
        return content;
    }
}
