package webserver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpReqeustBody {

    private final String content;

    public HttpReqeustBody(String content) {
        this.content = URLDecoder.decode(content, StandardCharsets.UTF_8);
    }

    public String getContent() {
        return content;
    }
}
