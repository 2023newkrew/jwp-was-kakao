package webserver.controller;

import constant.HttpMethod;
import model.annotation.Api;
import model.HttpRequest;
import webserver.dao.UserDao;

import java.io.DataOutputStream;

import static utils.ResponseUtils.*;

public class UserController extends ApiController {
    private static final UserController instance;
    private UserController() {}

    static {
        instance = new UserController();
    }
    public static UserController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.POST, url = "/user/create")
    public void register(HttpRequest request, DataOutputStream dos) {
        new UserDao().saveUser(request);

        response302Header(dos, "/index.html");
    }
}
