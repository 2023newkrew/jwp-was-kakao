package webserver.http.cookie;

public class HttpCookie {
    private final String name;
    private final String value;

    public HttpCookie(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
