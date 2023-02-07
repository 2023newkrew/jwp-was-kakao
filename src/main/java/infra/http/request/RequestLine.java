package infra.http.request;

public class RequestLine {
    public static String DELIMITER = " ";

    private HttpRequestMethod method;
    private String uri;
    private String version;

    public RequestLine(HttpRequestMethod method, String uri, String version) {
        this.version = version;
        this.method = method;
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public String getUri() {
        return this.uri;
    }

    public boolean isGET() {
        return this.method.equals(HttpRequestMethod.GET);
    }

    public boolean isPOST() {
        return this.method.equals(HttpRequestMethod.POST);
    }
}