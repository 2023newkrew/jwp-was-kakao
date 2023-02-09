package controller;

import model.User;
import service.UserService;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserSaveController implements PostMethodController {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        User newUser = new User(
                httpRequest.getParameter("userId"),
                httpRequest.getParameter("password"),
                httpRequest.getParameter("name"),
                httpRequest.getParameter("email")
        );
        UserService.saveUser(newUser);

        String location = "/index.html";
        httpResponse.sendRedirect(location);
        throw new RedirectException(location);
    }
}
