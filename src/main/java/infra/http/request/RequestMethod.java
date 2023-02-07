package infra.http.request;

public enum HttpRequestMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    HttpRequestMethod(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
