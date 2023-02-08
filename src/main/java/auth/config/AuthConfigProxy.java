package auth.config;

import auth.db.AuthLoginDatabase;
import infra.session.SessionManager;
import java.util.Objects;

public class AuthConfigProxy extends AuthConfig {

    private static AuthConfig INSTANCE = null;

    public static AuthConfig createInstance(AuthLoginDatabase authLoginDatabase, SessionManager sessionManager) {
        if(Objects.isNull(INSTANCE)) {
            INSTANCE = new AuthConfig(authLoginDatabase, sessionManager);
        }
        return INSTANCE;
    }
    private AuthConfigProxy(AuthLoginDatabase authLoginDatabase, SessionManager sessionManager) {
        super(authLoginDatabase, sessionManager);
    }

}
