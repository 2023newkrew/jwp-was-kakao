package webserver.content;

public enum ContentType {
    TEXT_HTML("text/html;charset=utf-8"),
    TEXT_CSS("text/css;charset=utf-8"),
    FONT_EOT("text/css;charset=utf-8"),
    IMAGE_PNG("image/png;charset=utf-8");

    final String header;

    ContentType(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return header;
    }
}
