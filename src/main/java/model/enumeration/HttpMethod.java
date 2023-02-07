package model.enumeration;

import java.util.Optional;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static Optional<HttpMethod> of(String string) {
        try {
            return Optional.of(HttpMethod.valueOf(string));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
