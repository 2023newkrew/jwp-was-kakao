package webserver.controller;

import model.annotation.Api;
import model.enumeration.HttpMethod;
import model.request.HttpRequest;
import utils.ResponseUtils;
import webserver.dao.UserDao;
import webserver.service.LoginService;
import webserver.service.UserService;

import javax.xml.crypto.Data;
import java.io.DataOutputStream;

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
    public void login(HttpRequest request, DataOutputStream dos) {
        loginService.login(request);
    }
}
