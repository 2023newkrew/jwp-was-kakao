package webserver.filter;

import webserver.http.request.HttpRequest;
import webserver.http.session.HttpSession;
import webserver.http.session.SessionManager;

public class SessionRequestFilter implements RequestFilter {
    public void doFilter(HttpRequest request) {
        HttpSession session = request.getCookie(HttpSession.JSESSIONID)
                .flatMap(SessionManager::findSession)
                .orElseGet(SessionManager::create);

        request.setSession(session);
    }
}
