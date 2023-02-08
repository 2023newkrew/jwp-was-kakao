package web.infra;

import java.util.concurrent.TimeUnit;

public interface SessionManager {

    void setAttribute(String key, Object value);
    Object getAttribute(String key);
    void removeAttribute(String key);
    void setExpirationTime(long expirationTime, TimeUnit unit);
    long getExpirationTime();

}
