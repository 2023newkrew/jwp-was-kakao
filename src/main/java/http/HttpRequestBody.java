package http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

class HttpRequestBody {

    private final String content;
    private final Map<String, String> params;

    protected HttpRequestBody(String content) {
        this.content = URLDecoder.decode(content, StandardCharsets.UTF_8);
        this.params = QueryParser.parse(this.content);
    }

    public String getContent() {
        return content;
    }

    public Optional<String> getParam(String key) {
        return Optional.ofNullable(params.get(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestBody that = (HttpRequestBody) o;
        return Objects.equals(content, that.content) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, params);
    }

    @Override
    public String toString() {
        return "HttpRequestBody{" +
                "content='" + content + '\'' +
                ", params=" + params +
                '}';
    }
}
