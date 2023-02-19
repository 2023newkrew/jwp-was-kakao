package filter;

import exception.UnauthorizedException;
import http.cookie.HttpCookie;
import http.request.Request;
import http.session.Session;
import http.session.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AuthorizedFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizedFilter.class);
    private static final SessionManager sessionManager = SessionManager.getInstance();
    private static final String JSESSIONID = "JSESSIONID";

    private AuthorizedFilter() {
    }

    private static class AuthorizationFilterHolder {
        private static final AuthorizedFilter instance = new AuthorizedFilter();
    }

    public static AuthorizedFilter getInstance() {
        return AuthorizationFilterHolder.instance;
    }

    public void doFilter(Request request) {
        HttpCookie cookie = request.getHeaders().getCookie();
        String sessionId = cookie.get(JSESSIONID).orElseThrow(UnauthorizedException::new);
        Session session = sessionManager.findSession(sessionId);
        if (Objects.isNull(session)) {
            throw new UnauthorizedException();
        }
        User user = (User) session.getAttribute("user");
        if (Objects.isNull(user)) {
            throw new UnauthorizedException();
        }
    }
}
