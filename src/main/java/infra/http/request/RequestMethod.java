package infra.http.request;

public enum RequestMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
