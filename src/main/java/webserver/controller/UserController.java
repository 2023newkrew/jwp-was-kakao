package webserver.controller;

import constant.HttpMethod;
import model.annotation.Api;
import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dao.UserDao;
import webserver.service.UserService;

import java.io.DataOutputStream;

import static utils.ResponseUtils.*;

public class UserController extends ApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
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
