package webserver.controller;

import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.annotation.Api;
import webserver.dao.UserDao;
import webserver.service.UserService;

import java.io.DataOutputStream;

import static utils.ResponseUtils.*;

public class UserController extends ApiController {
    private static final UserController instance;

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    static {
        instance = new UserController(new UserService(new UserDao()));
    }

    public static UserController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.POST, url = "/user/create")
    public void register(HttpRequest request, DataOutputStream dos) {
        userService.addUser(request);

        response302Header(dos, "/index.html");
    }
}
