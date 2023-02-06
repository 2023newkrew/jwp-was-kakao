package webserver.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    HttpMethod(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
