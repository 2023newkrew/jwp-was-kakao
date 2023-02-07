package webserver.controller;

import constant.HeaderConstant;
import model.annotation.Api;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.ResponseUtils;
import webserver.dao.UserDao;
import webserver.service.LoginService;
import webserver.service.UserService;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.util.Optional;
import java.util.UUID;

import static constant.HeaderConstant.*;
import static utils.ResponseUtils.*;

public class LoginController extends ApiController{
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

    @Api(method = HttpMethod.POST, url = "/user/login")
    public void login(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        Optional<UUID> loginUUID = loginService.login(request);

        if (loginUUID.isEmpty()) {
            response302Header(dos, "/user/login_failed.html");
            return;
        }
        response.setAttribute(SET_COOKIE, "JSESSIONID=" + loginUUID.get() + "; Path=/");
        response200Header(dos, response);
    }
}
