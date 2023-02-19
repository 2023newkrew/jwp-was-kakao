package webserver.handler;

import db.DataBase;
import http.*;
import model.User;
import http.HttpSession;
import webserver.session.HttpSessionManager;

import java.util.*;

public class UserLoginRequestHandler implements UrlMappingHandler {
    private static final String URL_MAPPING_REGEX = "/user/login";

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!hasUserIdAndPasswordParams(httpRequest) || !authenticateUser(httpRequest)) {
            responseLoginFail(httpResponse);
            return;
        }
        loginSuccess(httpRequest, httpResponse);
    }

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.POST;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }

    private boolean hasUserIdAndPasswordParams(HttpRequest httpRequest) {
        HttpRequestParams params = httpRequest.getParameters();
        return params.hasParameter("userId") && params.hasParameter("password");
    }

    private boolean authenticateUser(HttpRequest httpRequest) {
        HttpRequestParams params = httpRequest.getParameters();
        String userId = params.getParameter("userId");
        String password = params.getParameter("password");

        User user = DataBase.findUserById(userId);

        return user != null && Objects.equals(user.getPassword(), password);
    }

    private void responseLoginFail(HttpResponse httpResponse) {
        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.addHeader(HttpHeaders.LOCATION, List.of("/user/login_failed.html"));
    }

    private void loginSuccess(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpHeaders headers = new HttpHeaders();

        setSession(httpRequest, headers);
        headers.setHeader(HttpHeaders.LOCATION, List.of("/index.html"));

        httpResponse.setStatus(HttpStatus.FOUND);
        httpResponse.addHeaders(headers);
    }

    private void setSession(HttpRequest httpRequest, HttpHeaders headers) {
        String sessionId = UUID.randomUUID().toString();

        String userId = httpRequest.getParameters().getParameter("userId");
        User user = DataBase.findUserById(userId);

        HttpSession httpSession = new HttpSession(sessionId);
        httpSession.setAttribute("user", user);
        HttpSessionManager.add(httpSession);

        List<String> cookieValues = headers.getHeader(HttpHeaders.COOKIE);
        if (cookieValues == null) {
            cookieValues = new ArrayList<>();
        }

        HttpCookie sessionIdCookie =
                new HttpCookie(HttpSessionManager.SESSION_ID, sessionId, "/");
        cookieValues.add(sessionIdCookie.toHeaderValue());

        headers.setHeader(HttpHeaders.SET_COOKIE, cookieValues);
    }
}
