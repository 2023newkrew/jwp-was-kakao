package session;

import exceptions.AuthorizationException;
import http.HttpRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {
    private static final SessionManager sessionManager = SessionManager.getInstance();

    public static void validateSession(HttpRequest request) {
        String sessionId = request.getSessionCookie()
                .orElseThrow(AuthorizationException::new)
                .getSessionId()
                .orElseThrow(AuthorizationException::new);
        if (sessionManager.findSession(sessionId)
                .isEmpty()) {
            throw new AuthorizationException();
        }
    }
}
