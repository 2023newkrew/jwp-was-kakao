package http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

class HttpRequestBody {

    private final String content;
    private final Map<String, String> params;

    public HttpRequestBody(String content) {
        this.content = URLDecoder.decode(content, StandardCharsets.UTF_8);
        this.params = QueryParser.parse(this.content);
    }

    public String getContent() {
        return content;
    }

    public Optional<String> getParam(String key) {
        return Optional.ofNullable(params.get(key));
    }
}
