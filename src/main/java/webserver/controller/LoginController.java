package webserver.controller;

import constant.HeaderConstant;
import model.annotation.Api;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.user.User;
import utils.builder.ResponseBuilder;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.LoginService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static constant.DefaultConstant.DEFAULT_PAGE;
import static constant.HeaderConstant.*;

public class LoginController extends ApiController {
    private static final LoginController instance;

    private final LoginService loginService;

    static {
        instance = new LoginController(new LoginService(new UserDao()));
    }

    private LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public static LoginController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.GET, url = "/user/login.html")
    public HttpResponse showLoginPage(HttpRequest request) {
        return ViewResolver.resolve(request);
    }

    @Api(method = HttpMethod.POST, url = "/user/login", consumes = ContentType.APPLICATION_URL_ENCODED)
    public HttpResponse login(HttpRequest request) throws IOException {
        Optional<User> loginUser = loginService.login(request);

        if (loginUser.isEmpty()) {
            return ResponseBuilder.found("/user/login_failed.html");
        }

        HttpResponse response = ResponseBuilder.found(DEFAULT_PAGE);
        response.setHeaderAttribute(SET_COOKIE, "JSESSIONID=" + UUID.randomUUID());

        return response;
    }

}
