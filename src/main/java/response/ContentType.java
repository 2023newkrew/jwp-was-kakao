package response;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("application/javascript;charset=utf-8"),
    WOFF("font/*"),
    WOFF2("font/*"),
    TTF("application/x-font-ttf"),
    ICO("image/x-icon"),
    PNG("image/apng");

    private final String string;

    ContentType(String toResponseText) {
        this.string = toResponseText;
    }

    public String getString() {
        return string;
    }
}
