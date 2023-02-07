package webserver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302, "Found");

    private final int statusCode;

    private final String statusMessage;
}
