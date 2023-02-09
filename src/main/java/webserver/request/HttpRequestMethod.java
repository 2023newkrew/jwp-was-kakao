package webserver.request;

public enum HttpRequestMethod {
    GET,
    POST,
    ;

    public static HttpRequestMethod from(String method) {
        return HttpRequestMethod.valueOf(method.toUpperCase());
    }
}
