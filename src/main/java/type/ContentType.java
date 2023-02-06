package type;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JS("application/javascript;charset=utf-8"),
    WOFF("font/woff"),
    TTF("application/x-font-ttf"),
    ICO("image/x-icon"),
    ;

    private String toResponseText;

    ContentType(String toResponseText) {
        this.toResponseText = toResponseText;
    }

    public String getToResponseText() {
        return toResponseText;
    }
}
