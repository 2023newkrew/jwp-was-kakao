package http;

import error.ApplicationException;

import java.util.Arrays;
import java.util.List;

import static error.ErrorType.UNSUPPORTED_CONTENT_TYPE;

public enum ContentType {

    TEXT_HTML("text/html", "utf-8", "html", "htm"),
    TEXT_CSS("text/css", "utf-8", "css"),
    TEXT_PLAIN("text/plain", "utf-8"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "utf-8")
    ;

    private final String contentType;
    private final String charset;
    private final List<String> suffix;

    ContentType(String contentType, String charset, String... suffix) {
        this.contentType = contentType;
        this.charset = charset;
        this.suffix = List.of(suffix);
    }

    public static ContentType from(String resourcePath) {
        String suffix = resourcePath.substring(resourcePath.indexOf(".") + 1);

        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.suffix.contains(suffix))
                .findAny()
                .orElseThrow(() -> new ApplicationException(UNSUPPORTED_CONTENT_TYPE));
    }

    public String toString() {
        return String.format("%s;charset=%s", contentType, charset);
    }

}
