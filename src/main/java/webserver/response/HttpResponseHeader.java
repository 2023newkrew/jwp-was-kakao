package webserver.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpResponseHeader {

    public static final String SET_COOKIE = "Set-Cookie";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";
    private final Map<String, String> header;

    public HttpResponseHeader() {
        this(new HashMap<>());
    }

    public HttpResponseHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setLocation(String location) {
        header.put(LOCATION, location);
    }

    public void setContentType(String contentType) {
        header.put(CONTENT_TYPE, contentType + ";charset=utf-8");
    }

    public void setContentLength(int contentLength) {
        if (contentLength > 0) {
            header.put(CONTENT_LENGTH, String.valueOf(contentLength));
        }
    }

    public void setCookie(String key, String value) {
        if (header.containsKey(SET_COOKIE)) {
            header.put(SET_COOKIE, String.format("%s; %s=%s", header.get(SET_COOKIE), key, value));
        }
        header.put(SET_COOKIE, String.format("%s=%s", key, value));
    }

    public Set<Entry<String, String>> entrySet() {
        return header.entrySet();
    }
}
