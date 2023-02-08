package web.infra;

import java.time.LocalDateTime;

public class Session {

    private final Object value;
    private final LocalDateTime expiredAt;

    public Session(Object value, long expirationTime) {
        this.value = value;
        this.expiredAt = LocalDateTime.now().plusSeconds(expirationTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public Object getValue() {
        return value;
    }
}
