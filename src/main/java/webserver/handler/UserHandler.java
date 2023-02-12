package webserver.handler;

import service.UserService;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

public class UserHandler extends AbstractHandler {
    @Override
    protected void doGet(final HttpRequest request, final HttpResponse response) {
        addUser(request, response);
    }

    @Override
    protected void doPost(final HttpRequest request, final HttpResponse response) {
        addUser(request, response);
    }

    private static void addUser(final HttpRequest request, final HttpResponse response) {
        UserService.addUser(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        response.setHeader("Location", "/index.html");
        response.setStatus(HttpStatus.FOUND);
    }
}
