package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public class HttpRequestHeader {

    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String COOKIE = "Cookie";
    private final Map<String, String> headers;
    private final Cookie cookie;


    public HttpRequestHeader(Map<String, String> headers, Cookie cookie) {
        this.headers = headers;
        this.cookie = cookie;
    }

    public static HttpRequestHeader parse(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> headers = new HashMap<>();
        while (!StringUtils.isEmpty(line)) {
            String[] tokens = line.split(":", 2);
            headers.put(tokens[0], tokens[1].trim());
            line = bufferedReader.readLine();
        }
        Cookie cookie = Cookie.parse(headers.getOrDefault(COOKIE, ""));
        return new HttpRequestHeader(headers, cookie);
    }
    
    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, "0"));
    }

    public Cookie getCookie() {
        return cookie;
    }
}
