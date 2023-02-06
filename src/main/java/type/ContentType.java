package type;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JS("application/javascript;charset=utf-8"),
    WOFF("font/woff"),
    WOFF2("font/woff2"),
    TTF("application/x-font-ttf"),
    ICO("image/x-icon");

    private final String string;

    ContentType(String toResponseText) {
        this.string = toResponseText;
    }

    public String getString() {
        return string;
    }
}
