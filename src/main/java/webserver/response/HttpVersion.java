package webserver.response;

public enum HttpVersion {
    HTTP1_1("HTTP/1.1");

    private final String httpVersion;

    HttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
