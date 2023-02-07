package http;

import error.ApplicationException;

import java.util.Arrays;
import java.util.List;

import static error.ErrorType.UNSUPPORTED_CONTENT_TYPE;

public enum ContentType {

    TEXT_HTML("text/html", "html", "htm"),
    TEXT_CSS("text/css", "css"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    TEXT_PLAIN("text/plain"),
    IMAGE_X_ICON("image/x-icon", "ico"),
    APPLICATION_X_FONT_TTF("application/x-font-ttf", "ttf"),
    APPLICATION_X_FONT_WOFF("application/x-font-woff", "woff"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    ;

    private final String contentType;
    private final String charset;
    private final List<String> suffix;

    ContentType(String contentType, String... suffix) {
        this.contentType = contentType;
        this.charset = "utf-8";
        this.suffix = List.of(suffix);
    }

    public static ContentType from(String resourcePath) {
        String suffix = resourcePath.substring(resourcePath.lastIndexOf(".") + 1);

        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.suffix.contains(suffix))
                .findAny()
                .orElseThrow(() -> new ApplicationException(UNSUPPORTED_CONTENT_TYPE));
    }

    public String toString() {
        return String.format("%s;charset=%s", contentType, charset);
    }

}
