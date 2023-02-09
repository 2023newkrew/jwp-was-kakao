package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
    private final Map<String, String> headers;
    private final Cookie cookie;


    public HttpRequestHeader(Map<String, String> headers, Cookie cookie) {
        this.headers = headers;
        this.cookie = cookie;
    }

    public static HttpRequestHeader parse(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> headers = new HashMap<>();
        while (!isNullOrEmpty(line)) {
            String[] tokens = line.split(":");
            headers.put(tokens[0], tokens[1].trim());
            line = bufferedReader.readLine();
        }
        Cookie cookie = Cookie.parse(headers.getOrDefault("Cookie", ""));
        return new HttpRequestHeader(headers, cookie);
    }

    private static boolean isNullOrEmpty(String line) {
        return line == null || "".equals(line);
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
    }

    public Cookie getCookie() {
        return cookie;
    }
}
