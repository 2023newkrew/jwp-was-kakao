package webserver.filter;

import webserver.http.Cookie;
import webserver.http.Session;
import webserver.http.SessionManager;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.util.Optional;

public class SessionFilter implements MyFilter {

    private SessionFilter() {
    }

    private static class SessionFilterHolder {
        private static final SessionFilter INSTANCE = new SessionFilter();
    }

    public static SessionFilter getInstance() {
        return SessionFilterHolder.INSTANCE;
    }


    @Override
    public void doFilter(HttpRequest request, HttpResponse.Builder responseBuilder, MyFilterChain filterChain) {
        Session session = createSession(request, responseBuilder);
        request.setSession(session);
        filterChain.doFilter(request, responseBuilder);
    }

    private Session createSession(HttpRequest request, HttpResponse.Builder responseBuilder) {
        Optional<Session> findValidSession = request.getCookies()
                .stream()
                .filter(cookie -> "JSESSIONID" .equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(SessionManager.getInstance()::isValid)
                .map(SessionManager.getInstance()::findSession)
                .findFirst();
        if (findValidSession.isEmpty()) {
            Session session = new Session();
            SessionManager.getInstance()
                    .add(session);
            Cookie cookie = new Cookie("JSESSIONID", session.getSessionId());
            responseBuilder.addHeader("Set-Cookie", String.valueOf(cookie));
            return session;
        }
        return findValidSession.get();
    }
}
