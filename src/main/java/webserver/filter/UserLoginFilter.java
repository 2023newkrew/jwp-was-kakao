package webserver.filter;

import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import webserver.session.HttpSessionManager;

import java.util.List;

public class UserLoginFilter implements Filter {
    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse, FilterChain filterChain) {
        String sessionId = getSessionId(httpRequest);
        if (sessionId != null) {
            setSession(httpRequest, sessionId);
        }
        filterChain.doFilter(httpRequest, httpResponse);
    }

    private String getSessionId(HttpRequest httpRequest) {
        HttpHeaders headers = httpRequest.getHeaders();
        List<String> cookieValues = headers.getHeader(HttpHeaders.COOKIE);

        if (cookieValues == null) {
            return null;
        }

        String value = cookieValues.stream()
                .filter(cookieValue -> cookieValue.contains(HttpSessionManager.SESSION_ID + "="))
                .findFirst()
                .orElse(null);

        if (value == null) {
            return null;
        }

        String sessionId = value.split("=", 2)[1];
        return sessionId.split(";", 2)[0].trim();
    }

    private static void setSession(HttpRequest httpRequest, String sessionId) {
        HttpSession session = HttpSessionManager.findSession(sessionId);
        if (session != null) {
            httpRequest.setSession(session);
        }
    }
}
