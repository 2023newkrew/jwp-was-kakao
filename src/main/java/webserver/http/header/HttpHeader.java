package webserver.http.header;

public class HttpHeader {
    private final String name;
    private final String value;

    public HttpHeader(final String name, final String value) {
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
