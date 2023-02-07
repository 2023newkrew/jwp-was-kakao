package http;

import java.util.Arrays;

public enum ContentType {
    TEXT_HTML(".html", "text/html"),
    TEXT_CSS(".css", "text/css"),
    TEXT_JAVASCRIPT(".js", "text/javascript"),
    IMAGE_PNG(".png", "image/png"),
    IMAGE_X_ICON(".ico", "image/x-icon"),
    IMAGE_SVG_XML(".svg", "image/svg+xml"),
    FONT_TTF(".ttf", "font/ttf"),
    FONT_WOFF(".woff", "font/woff"),
    FONT_WOFF2(".woff2", "font/woff2"),
    APPLICATION_VND_MS_FONTOBJECT(".eot", "application/vnd.ms-fontobject"),
    ;

    private final String extension;
    private final String value;

    ContentType(String extension, String value) {
        this.extension = extension;
        this.value = value;
    }

    public static ContentType of(String path) {
        return Arrays.stream(values())
                .filter(type -> path.endsWith(type.extension))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getExtension() {
        return extension;
    }

    public String getValue() {
        return value;
    }
}
