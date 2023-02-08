package model.enumeration;

import java.util.Arrays;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html"),
    APPLICATION_URL_ENCODED("application/x-www-form-urlencoded"),
    ANY("*/*");

    private final String value;

    public static ContentType of(String targetValue) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.value.equals(targetValue))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
