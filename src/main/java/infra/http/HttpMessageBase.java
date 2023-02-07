package infra.http;

public class HttpMessageBase {
    public static String LINE_DELIMITER = " \r\n";
    public static String BODY_DELIMITER = "\r\n";
    public static String DEFAULT_VERSION = "HTTP/1.1";
    private Headers headers;

    public HttpMessageBase(Headers headers) {
        this.headers = headers;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }
}
