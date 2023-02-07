package http;

import error.ApplicationException;

import java.util.Arrays;
import java.util.Objects;

import static error.ErrorType.UNSUPPORTED_PROTOCOL;

public enum Protocol {

    HTTP1_1("HTTP/1.1");

    private final String str;

    Protocol(String str) {
        this.str = str;
    }

    public String toString() {
        return str;
    }

    public static Protocol from(String str) {
        return Arrays.stream(Protocol.values())
                .filter(protocol -> Objects.equals(protocol.str, str))
                .findAny()
                .orElseThrow(() -> new ApplicationException(UNSUPPORTED_PROTOCOL));
    }

}
