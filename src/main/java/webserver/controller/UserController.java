package webserver.controller;

import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.annotation.Api;
import model.response.HttpResponse;
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
    public void register(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        userService.addUser(request);

        response.setHeaderAttribute(LOCATION, "/index.html");

//        response302Header(dos, response);
    }

    @Api(method = HttpMethod.GET, url = "/user/list.html")
    public void showUserList(HttpRequest request){
        if (request.findHeaderValue(COOKIE,  null) == null) {
//            response.setHeaderAttribute(LOCATION, "/index.html");
//            response302Header(dos, response);
            return;
        }

        ViewResolver.resolve(handleUserListTemplate());
    }
}
