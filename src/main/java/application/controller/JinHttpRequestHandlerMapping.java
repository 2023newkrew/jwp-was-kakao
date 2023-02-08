package application.controller;

import webserver.enums.RequestMethod;
import webserver.exceptions.HandlerNotFoundException;
import webserver.http.requesthandler.HttpRequestHandlerMapping;
import webserver.http.requesthandler.HttpRequestHandler;
import webserver.http.request.HttpRequest;

public class JinHttpRequestHandlerMapping implements HttpRequestHandlerMapping {
    private final HomeController homeController;
    private final UserController userController;

    public JinHttpRequestHandlerMapping() {
        this.homeController = HomeController.getInstance();
        this.userController = UserController.getInstance();
    }

    @Override
    public HttpRequestHandler findHandler(HttpRequest request) throws HandlerNotFoundException {
        String requestURL = request.getRequestURL();

        // UserController
        if (requestURL.startsWith("/user/create") && request.getRequestMethod() == RequestMethod.GET) {
            return userController::createUserGet;
        }
        if (requestURL.startsWith("/user/create") && request.getRequestMethod() == RequestMethod.POST) {
            return userController::createUserPost;
        }
        if (requestURL.startsWith("/user/login") && request.getRequestMethod() == RequestMethod.POST) {
            return userController::userLogin;
        }
        if (requestURL.startsWith("/user/list") && request.getRequestMethod() == RequestMethod.GET) {
            return userController::userList;
        }

        // HomeController
        if (requestURL.equals("/") && request.getRequestMethod() == RequestMethod.GET) {
            return homeController::rootPathGet;
        }

        throw new HandlerNotFoundException();
    }
}
