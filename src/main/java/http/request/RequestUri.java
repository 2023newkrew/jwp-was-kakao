package http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class RequestUri {
    private final String path;
    private final RequestParam requestParam;

    public RequestUri(String uri) {
        this.path = uri.split(Pattern.quote("?"), 2)[0];
        this.requestParam = parseParam(uri);
    }

    private RequestParam parseParam(String uri) {
        if (uri.contains("?")) {
            String query = uri.split(Pattern.quote("?"), 2)[1];
            query = URLDecoder.decode(query, StandardCharsets.UTF_8);
            return new RequestParam(query);
        }
        return new RequestParam();
    }

    public String getPath() {
        return path;
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }
}
