package webserver.http.header;

public enum HeaderType {
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    LOCATION("Location"),
    COOKIE("Cookie"),

    ;


    private final String name;

    HeaderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
