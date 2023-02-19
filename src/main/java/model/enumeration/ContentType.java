package model.enumeration;

import exception.NoSuchContentTypeException;

import java.util.Arrays;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html;charset=utf-8"),
    APPLICATION_URL_ENCODED("application/x-www-form-urlencoded"),
    ANY("*/*");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType of(String targetValue) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.value.equals(targetValue))
                .findFirst()
                .orElseThrow(NoSuchContentTypeException::new);
    }

    public String getValue() {
        return value;
    }
}
