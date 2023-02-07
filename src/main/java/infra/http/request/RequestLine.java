package infra.http.request;

public class RequestLine {
    public static String DELIMITER = " ";

    private RequestMethod method;
    private String uri;
    private String version;

    public RequestLine(RequestMethod method, String uri, String version) {
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

    public RequestMethod getMethod() {
        return method;
    }

    public boolean isGET() {
        return this.method.equals(RequestMethod.GET);
    }

    public boolean isPOST() {
        return this.method.equals(RequestMethod.POST);
    }
}