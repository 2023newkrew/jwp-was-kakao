package controller;

import service.UserService;
import webserver.*;

import java.util.Map;

public class UserController extends BaseController {
    private static final String REDIRECT_PATH = "/index.html";
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    void processRequestByMethod(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET) || request.getMethod().equals(HttpMethod.POST)) {
            Map<String, String> map = request.getParameter();
            userService.addUser(
                    map.get("userId"),
                    map.get("password"),
                    map.get("name"),
                    map.get("email")
            );
            response.setHeader(HttpHeader.LOCATION, REDIRECT_PATH);
            response.setHttpStatus(HttpStatus.FOUND);
        }
    }
}
