package infra.http.header;

public enum ContentType {
    X_WWW_FROM_URLENCODED("application/x-www-form-urlencoded"),
    UTF8_HTML("text/html;charset=utf-8"),
    UTF8_CSS("text/css;charset=utf-8");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
