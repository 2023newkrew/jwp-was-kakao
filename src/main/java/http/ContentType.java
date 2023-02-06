package http;

import java.util.Arrays;
import java.util.List;

public enum ContentType {

    TEXT_HTML("text/html", "utf-8", "html", "htm"),
    TEXT_CSS("text/css", "utf-8", "css"),
    TEXT_PLAIN("text/plain", "utf-8"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "utf-8")
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
        String suffix = resourcePath.substring(resourcePath.indexOf(".") + 1);

        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.suffix.contains(suffix))
                .findAny()
                .orElseThrow(() -> new RuntimeException());
    }

    public String toString() {
        return String.format("%s;charset=%s", contentType, charset);
    }

}
