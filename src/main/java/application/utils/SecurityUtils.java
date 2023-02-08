package application.utils;

import webserver.http.request.HttpRequest;

public class SecurityUtils {
    public static boolean isLoggedIn(HttpRequest request) {
        return request.getCookie("JSESSIONID").isPresent();
    }
}
