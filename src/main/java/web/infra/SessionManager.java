package web.infra;

import java.util.Optional;

public interface SessionManager {

    void setAttribute(String key, Object value);
    Optional<Object> getAttribute(String key);
    void remoteAttribute(String key);

}
