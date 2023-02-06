package http;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum ContentType {

    TEXT_HTML("text/html", "utf-8", "html", "htm"),
    TEXT_CSS("text/css", "utf-8", "css"),
    TEXT_PLAIN("text/plain", "utf-8"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "utf-8"),
    TEXT_JAVASCRIPT("text/javascript", "utf-8"),
    EMPTY("", "utf-8")
    ;

    private String contentType;
    private String charset;
    private List<String> suffix;

    ContentType(String contentType, String charset, String... suffix) {
        this.contentType = contentType;
        this.charset = charset;
        this.suffix = List.of(suffix);
    }

    public static ContentType from(String resourcePath) {
        String suffix = resourcePath.substring(resourcePath.lastIndexOf(".") + 1).equals("js")
                ? "javascript" : resourcePath.substring(resourcePath.lastIndexOf(".") + 1);

        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.suffix.contains(suffix))
                .findAny()
                .orElse(ContentType.EMPTY);
    }

    public String toString() {
        return String.format("%s;charset=%s", contentType, charset);
    }

}
