package webserver.security;

import webserver.request.HttpRequest;

import java.util.List;
import java.util.Optional;

public class SecurityHandler {

    private static final List<String> authenticatedUri = List.of(
            "/user/list"
    );

    public boolean isNeedAuthentication(String uri) {
        return authenticatedUri.contains(uri);
    }

    public boolean isNotAuthenticated(HttpRequest request) {
        Optional<String> userInfo = request.getCookie("JSESSIONID");
        return userInfo.isEmpty() || SessionManager.getInstance().findSession(userInfo.get()).isEmpty();
    }
}
