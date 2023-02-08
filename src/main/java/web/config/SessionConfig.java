package web.config;

import web.infra.MemorySessionManager;
import web.infra.SessionManager;

public enum SessionConfig {

    MEMORY_SESSION_MANAGER(new MemorySessionManager()),
    ;

    private final SessionManager sessionManager;

    SessionConfig(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public SessionManager getInstance() {
        return sessionManager;
    }
}
