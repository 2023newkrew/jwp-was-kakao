package controller;

import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;
import webserver.SessionManager;

import java.util.UUID;

public class LoginController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        String location = "/user/login_failed.html";
        if (UserService.isValidAuthentication(userId, password)) {
            location = "/index.html";

            String sessionId = UUID.randomUUID().toString();
            httpResponse.setJSessionId(sessionId);

            HttpSession session = new HttpSession(sessionId);
            session.setAttribute("user", UserService.findUserById(userId));
            SessionManager.add(session);
        }
        httpResponse.sendRedirect(location);
        throw new RedirectException(location);
    }
}
