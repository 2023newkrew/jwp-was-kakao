package webserver.controller;

import model.annotation.Api;
import model.annotation.ApiController;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.user.User;
import model.web.Cookie;
import utils.builder.CookieBuilder;
import utils.builder.ResponseBuilder;
import webserver.dao.UserDao;
import webserver.infra.ViewResolver;
import webserver.service.LoginService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static constant.DefaultConstant.*;
import static utils.utils.LoginUtils.*;

@ApiController
public class LoginController {
    private static final LoginController instance;

    static {
        instance = new LoginController(new LoginService(new UserDao()));
    }

    private final LoginService loginService;

    private LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public static LoginController getInstance() {
        return instance;
    }

    @Api(method = HttpMethod.GET, url = "/user/login.html")
    public HttpResponse showLoginPage(HttpRequest request) {
        if (isLogin(request)) {
            return ResponseBuilder.found(DEFAULT_PAGE);
        }
        return ViewResolver.resolve(request);
    }

    @Api(method = HttpMethod.POST, url = "/user/login", consumes = ContentType.APPLICATION_URL_ENCODED)
    public HttpResponse login(HttpRequest request) throws IOException {
        Optional<User> loginUser = loginService.login(request);

        if (loginUser.isEmpty()) {
            return ResponseBuilder.found("/user/login_failed.html");
        }

        HttpResponse response = ResponseBuilder.found(DEFAULT_PAGE);
        response.addCookie(getCookie(), loginUser.get());

        return response;
    }

    private Cookie getCookie() {
        return CookieBuilder.build(DEFAULT_SESSION_ID, UUID.randomUUID().toString());
    }
}
