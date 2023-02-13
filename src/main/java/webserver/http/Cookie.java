package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookie {

    private final Map<String, String> cookies = new HashMap<>();

    public Cookie() {

    }

    public Cookie(String cookieString) {
        if (cookieString == null || cookieString.isEmpty()) return;
        String[] tokens = cookieString.trim().split(";");
        Arrays.stream(tokens).map(token -> token.split("="))
                .filter(cookieTokens -> cookieTokens.length == 2)
                .forEach(cookieTokens -> cookies.put(cookieTokens[0].trim(), cookieTokens[1].trim()));
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    public void setCookie(String name, String value) {
        cookies.put(name, value);
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    public String toResponseHeaderLine() {
        StringBuilder sb = new StringBuilder();
        sb.append("Set-Cookie:");
        for (String name : cookies.keySet()) {
            sb.append(String.format(" %s=%s;", name, cookies.get(name)));
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    public boolean hasSessionId() {
        String sessionId = cookies.get(SessionManager.SESSION_ID_NAME);
        return sessionId != null && !sessionId.isEmpty();
    }

    @Override
    public String toString() {
        return "Cookie{" +
                cookies.keySet().stream().map(key -> key + "=" + cookies.get(key) )
                        .collect(Collectors.toList())
                + '}';
    }
}
