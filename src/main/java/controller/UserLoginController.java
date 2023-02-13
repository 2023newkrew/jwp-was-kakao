package controller;

import db.Database;
import exception.WasException;
import model.User;
import utils.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ModelAndView;

import java.util.Map;
import java.util.Optional;

import static exception.ErrorCode.USER_NOT_EXIST;

public class UserLoginController extends Controller {

    private final Database db;

    public UserLoginController(Database db) {
        this.db = db;
    }

    @Override
    protected ModelAndView run(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> params = IOUtils.extractParams(requestBody);

        String userId = params.get("userId");
        String password = params.get("password");

        User user = Optional.ofNullable(db.findUserById(userId))
                .orElseThrow(() -> new WasException(USER_NOT_EXIST));

        if (user.checkPassword(password)) {
            response.getResponseHeader().putCookieItem("logined", "true");
            return new ModelAndView("redirect:/index.html");
        }
        return new ModelAndView("redirect:/users/login_failed.html");
    }
}
