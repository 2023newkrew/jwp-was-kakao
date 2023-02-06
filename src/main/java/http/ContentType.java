package http;

import java.util.Arrays;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JS("js", "text/javascript;charset=utf-8"),
    WOFF("woff", "application/x-font-woff"),
    TTF("ttf", "application/x-font-ttf"),
    ICO("ico", "image/x-icon"),
    X_WWW_FORM_URLENCODED("", "application/x-www-form-urlencoded");

    private final String fileExtension;
    private final String value;

    ContentType(String fileExtension, String value) {
        this.fileExtension = fileExtension;
        this.value = value;
    }

    public static ContentType of(String extension) {
        return Arrays.stream(values()).filter(item -> extension.equals(item.fileExtension)).findAny().orElseThrow(IllegalArgumentException::new);
    }

    public static boolean isFileExtension(String extension) {
        try {
            of(extension);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getValue() {
        return value;
    }
}
