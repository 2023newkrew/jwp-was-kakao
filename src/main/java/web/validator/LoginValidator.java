package web.validator;

import http.HttpCookies;
import http.request.HttpRequest;
import web.infra.SessionManager;

import java.util.Objects;

import static http.HttpCookies.SESSION_ID;

public class LoginValidator {

    private final SessionManager sessionManager;

    public LoginValidator(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean validate(HttpRequest httpRequest) {
        HttpCookies httpCookies = httpRequest.getCookies();
        String sessionId = httpCookies.get(SESSION_ID);

        return Objects.nonNull(sessionId) && sessionManager.getAttribute(sessionId)
                .isPresent();
    }

}
