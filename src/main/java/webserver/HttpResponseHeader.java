package webserver;

import enums.ContentType;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    private final Map<String, String> headers;

    private HttpResponseHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpResponseHeader emptyHeader() {
        return new HttpResponseHeader(new HashMap<>());
    }

    public void setContentType(ContentType contentType) {
        headers.put(CONTENT_TYPE, contentType.getValue() + ";charset=utf-8");
    }

    public void setContentLength(int contentLength) {
        headers.put(CONTENT_LENGTH, String.valueOf(contentLength));
    }

    public void setLocation(String location) {
        headers.put(LOCATION, location);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
