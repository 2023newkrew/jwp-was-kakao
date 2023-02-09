package framework.request;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {
    private final Map<String, String> cookies;

    private HttpCookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static HttpCookie from(Map<String, String> cookies) {
        return new HttpCookie(cookies);
    }

    public static HttpCookie from(String cookies) {
        Map<String, String> cookieMap = new HashMap<>();
        for (String cookie: cookies.split(";")) {
            String[] split = cookie.trim().split("=");
            cookieMap.put(split[0].trim(), split[1].trim());
        }
        return new HttpCookie(cookieMap);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry: cookies.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return stringBuilder.toString();
    }
}
