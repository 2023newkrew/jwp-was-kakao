package model.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Session<T> {
    String getId();

    void setAttribute(final String key, T value);

    void removeAttribute(final String key);

    void invalidate();

    Optional<T> getAttribute(final String key);
}
