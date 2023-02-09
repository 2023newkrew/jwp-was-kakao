package controller;

import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        String location = "/user/login_failed.html";
        if (UserService.isValidAuthentication(userId, password)) {
            location = "/index.html";
            httpRequest.getSession().setAttribute("user", UserService.findUserById(userId));
        }
        httpResponse.sendRedirect(location);
        throw new RedirectException(location);
    }
}
