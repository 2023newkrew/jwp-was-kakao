package webserver.controller;

import model.annotation.Api;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.builder.ResponseBuilder;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.UserService;

import static constant.DefaultConstant.*;
import static utils.utils.LoginUtils.*;
import static utils.utils.TemplateUtils.*;

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

    @Api(method = HttpMethod.POST, url = "/user/create", consumes = ContentType.APPLICATION_URL_ENCODED)
    public HttpResponse register(HttpRequest request) {
        userService.addUser(request);

        return ResponseBuilder.found(DEFAULT_PAGE);
    }

    @Api(method = HttpMethod.GET, url = "/user/list.html")
    public HttpResponse showUserList(HttpRequest request) {
        if (!isLogin(request)) {
            return ResponseBuilder.found(DEFAULT_PAGE);
        }

        return ViewResolver.resolve(handleUserListTemplate());
    }
}
