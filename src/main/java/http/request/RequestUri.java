package http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class RequestUri {
    private final String path;
    private final RequestParam requestParam;

    public RequestUri(String uri) {
        String[] splitUri = uri.split(Pattern.quote("?"), 2);
        this.path = splitUri[0];
        this.requestParam = parseParam(splitUri);
    }

    private RequestParam parseParam(String[] splitUri) {
        if (splitUri.length >= 2) {
            String query = URLDecoder.decode(splitUri[1], StandardCharsets.UTF_8);
            return new RequestParam(query);
        }
        return RequestParam.empty();
    }

    public String getPath() {
        return path;
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }
}
