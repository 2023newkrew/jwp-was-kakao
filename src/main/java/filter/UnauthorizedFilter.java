package filter;

import exception.AuthorizedException;
import http.cookie.HttpCookie;
import http.request.Request;
import http.session.Session;
import http.session.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class UnauthorizedFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedFilter.class);
    private static final SessionManager sessionManager = SessionManager.getInstance();
    private static final String JSESSIONID = "JSESSIONID";

    private UnauthorizedFilter() {
    }

    private static class UnauthorizedFilterHolder {
        private static final UnauthorizedFilter instance = new UnauthorizedFilter();
    }

    public static UnauthorizedFilter getInstance() {
        return UnauthorizedFilterHolder.instance;
    }

    public void doFilter(Request request) {
        HttpCookie cookie = request.getHeaders().getCookie();
        Optional<String> sessionId = cookie.get(JSESSIONID);
        if (sessionId.isEmpty()) {
            return;
        }
        Session session = sessionManager.findSession(sessionId.get());
        if (Objects.isNull(session)) {
            return;
        }
        User user = (User) session.getAttribute("user");
        if (Objects.isNull(user)) {
            return;
        }
        throw new AuthorizedException();
    }
}
