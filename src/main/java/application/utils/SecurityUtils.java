package application.utils;

import application.dto.UserResponse;
import webserver.http.request.HttpRequest;
import webserver.http.session.HttpSession;

import java.util.Optional;

public class SecurityUtils {
    public static boolean isLoggedIn(HttpRequest request) {
        return request.getSession()
                .getAttribute(HttpSession.USER)
                .isPresent();
    }

    public static UserResponse getLoggedInUser(HttpRequest request) {
        return request.getSession()
                .getAttribute(HttpSession.USER)
                .map(UserResponse.class::cast)
                .get();
    }

    public static void setUserToSession(HttpRequest request, UserResponse user) {
        request.getSession()
                .setAttribute(HttpSession.USER, user);
    }
}
