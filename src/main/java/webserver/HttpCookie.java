package webserver;

import java.util.*;

public class HttpCookie {

    private final Map<String, String> cookieMap = new HashMap<>();

    public HttpCookie() {}

    public HttpCookie(String cookie) {
        String[] datas = cookie.split(";");
        for (String data : datas) {
            String[] line = data.split("=");
            // TODO: 개선할 수 없을까?
            if (line.length == 2) {
                cookieMap.put(line[0].trim(), line[1].trim());
            }
        }
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(cookieMap.get(key));
    }

    public void put(String key, String value) {
        cookieMap.put(key, value);
    }

    public String parseString() {
        StringBuilder sb = new StringBuilder();
        for (String key : cookieMap.keySet()) {
            //TODO: 더 나은 방식으로 변경해야 함
            if (!key.equals("Path")) {
                sb.append("Set-Cookie: ").append(key).append("=").append(cookieMap.get(key)).append("; Path=/").append("\r\n");
            }
        }
        return sb.toString();
    }
}
