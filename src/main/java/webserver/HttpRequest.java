package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import utils.IOUtils;

public class HttpRequest {
    private static final String CONTENT_LENGTH = "Content-Length";

    private final HttpRequestLine requestLine;
    private final Map<String, String> headers;

    private final String body;

    private HttpRequest(HttpRequestLine requestLine, Map<String, String> headers, String body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest from(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String startLine = bufferedReader.readLine();
        Map<String, String> headers = readHeader(bufferedReader);
        String body = readBody(bufferedReader, headers);
        return new HttpRequest(HttpRequestLine.from(startLine), headers, body);
    }

    private static Map<String, String> readHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            String key = line.split(":")[0].trim();
            String value = line.split(":")[1].trim();
            headers.put(key, value);
            line = bufferedReader.readLine();
        }
        return headers;
    }

    private static String readBody(BufferedReader bufferedReader, Map<String, String> headers) throws IOException {
        if (headers.containsKey(CONTENT_LENGTH)) {
            return IOUtils.readData(bufferedReader, Integer.parseInt(headers.get(CONTENT_LENGTH)));
        }
        return "";
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getParameter(String key) {
        return requestLine.getParameter(key);
    }

    public String getBody() {
        return body;
    }

    public Session getSession() {
        HttpCookies cookies = getCookies();
        String sessionId = cookies.getCookies().stream()
                .filter(cookie -> cookie.getKey().equals("JSESSIONID"))
                .map(HttpCookie::getValue)
                .findAny()
                .orElse("");

        return SessionManager.findSession(sessionId);
    }

    public HttpCookies getCookies() {
        String cookies = headers.get("Cookie");
        HttpCookies result = new HttpCookies();

        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                String key = cookie.split("=")[0];
                String value = cookie.split("=")[1];
                result.addCookie(new HttpCookie(key, value));
            }
        }

        return result;
    }

    public Map<String, String> toApplicationForm() {
        return Arrays.stream(body.split("&"))
                .map(v -> v.split("="))
                .collect(Collectors.toMap(
                        v -> v[0],
                        v -> URLDecoder.decode(v[1], Charset.defaultCharset())
                ));
    }
}
