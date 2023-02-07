package webserver;

public class HttpResponse {
    final String version;
    final String status;
    final String[] headers;
    final byte[] body;

    public HttpResponse(String version, String status, String[] headers, byte[] body) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }
}
