package controller;

import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.UUID;

public class LoginController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        String location = "/user/login_failed.html";
        if (UserService.isValidAuthentication(userId, password)) {
            location = "/index.html";
            UUID sessionId = UUID.randomUUID();
            httpResponse.setJSessionId(sessionId);
        }
        httpResponse.sendRedirect(location);
        throw new RedirectException(location);
    }
}
