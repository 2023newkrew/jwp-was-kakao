package webserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpHeaderProperties {

    CONTENT_TYPE("Content-Type"),

    CONTENT_LENGTH("Content-Length"),

    LOCATION("Location");

    private final String key;
}
