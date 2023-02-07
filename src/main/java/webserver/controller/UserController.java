package webserver.controller;

import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.annotation.Api;
import model.response.HttpResponse;
import utils.ResponseBuilder;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.UserService;

import java.io.DataOutputStream;
import java.io.IOException;

import static constant.HeaderConstant.*;
import static utils.ResponseUtils.*;
import static utils.TemplateUtils.*;

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
    public HttpResponse register(HttpRequest request) {
        userService.addUser(request);

        return ResponseBuilder.found("/index.html");
    }

    @Api(method = HttpMethod.GET, url = "/user/list.html")
    public HttpResponse showUserList(HttpRequest request){
        if (request.findHeaderValue(COOKIE,  null) == null) {
            return ResponseBuilder.found("/index.html");
        }

        return ViewResolver.resolve(handleUserListTemplate());
    }
}
