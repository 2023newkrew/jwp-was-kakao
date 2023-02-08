package webserver;

import java.util.Map;
import utils.HttpRequestUtils;

public class HttpCookie {
    public static final String SESSION_ID_NAME = "JSESSIONID";
    private final Map<String, String> cookies;

    public HttpCookie(String cookieString) {
        this.cookies = HttpRequestUtils.parseCookie(cookieString);
    }

    public String getCookie(String name) {
        if (!cookies.containsKey(name)) {
            return null;
        }
        return cookies.get(name);
    }
}
