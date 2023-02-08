package webserver.http.response;

import webserver.enums.ContentType;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE = "Set-Cookie";
    private final Map<String, String> headers;

    private HttpResponseHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public static HttpResponseHeader emptyHeader() {
        return new HttpResponseHeader(new HashMap<>());
    }

    public void contentType(ContentType contentType) {
        headers.put(CONTENT_TYPE, contentType.getValue() + ";charset=utf-8");
    }

    public void contentLength(int contentLength) {
        headers.put(CONTENT_LENGTH, String.valueOf(contentLength));
    }

    public void location(String location) {
        headers.put(LOCATION, location);
    }

    public void setCookie(String key, String value) {
        String existingSetCookie = headers.get(SET_COOKIE);
        if (existingSetCookie != null) {
            headers.put(SET_COOKIE, String.format("%s; %s=%s", existingSetCookie, key, value));
            return;
        }
        headers.put(SET_COOKIE, String.format("%s=%s", key, value));
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
