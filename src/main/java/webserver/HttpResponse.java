package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private final String statusLine;
    private final Map<String, String> header;
    private final byte[] body;

    public HttpResponse(String statusLine, Map<String, String> header, byte[] body) {
        this.statusLine = statusLine;
        this.header = new HashMap<>(header);
        if (body.length > 0) {
            this.header.put("Content-Length", Integer.toString(body.length));
        }
        this.body = body;
    }

    public static HttpResponse ok(byte[] body) {
        return HttpResponse.ok(Map.of(), body);
    }

    public static HttpResponse ok(Map<String, String> headers, byte[] body) {
        return new HttpResponse("HTTP/1.1 200 OK", headers, body);
    }

    public static HttpResponse redirect(String location) {
        return HttpResponse.redirect(Map.of(), location);
    }

    public static HttpResponse redirect(Map<String, String> headers, String location) {
        return HttpResponse.redirect(headers, new HttpCookies(), location);
    }

    public static HttpResponse redirect(Map<String, String> headers, HttpCookies cookies, String location) {
        Map<String, String> newHeaders = new HashMap<>(headers);
        newHeaders.put("Location", location);
        return new HttpResponse("HTTP/1.1 302 Found", newHeaders, "".getBytes());
    }

    public HttpResponse setContentType(String contentType) {
        header.put("Content-Type", contentType);
        return this;
    }

    public HttpResponse setCookie(HttpCookies cookies) {
        String value = cookies.getCookies().stream()
                .map(cookie -> cookie.getKey() + "=" + cookie.getValue())
                .collect(Collectors.joining(";"));
        header.put("Set-Cookie", value);
        return this;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
