package controller;

import common.*;
import service.UserService;
import support.PathNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class UserController implements Controller {
    private static final String REDIRECT_PATH = "/index.html";
    private static Map<String, BiConsumer<HttpRequest, HttpResponse>> mapping = new HashMap<>();

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

        // 요청 url -> method 매핑
        mapping.put("/user/create", this::createUser);
    }

    public void process(HttpRequest request, HttpResponse response) {
        if (Objects.isNull(mapping.get(request.getUri()))) {
            throw new PathNotFoundException();
        }
        mapping.get(request.getUri()).accept(request, response);
    }

    public void createUser(HttpRequest request, HttpResponse response) {
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
