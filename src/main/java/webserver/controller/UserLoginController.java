package webserver.controller;

import db.DataBase;
import http.Cookie;
import http.HttpResponse;
import http.ResponseStatus;
import http.request.HttpRequest;
import http.request.RequestParam;
import model.User;

import java.util.Optional;
import java.util.UUID;

public class UserLoginController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        RequestParam requestParam = request.getRequestParam();
        response.setResponseStatus(ResponseStatus.FOUND);

        String userId = requestParam.get("userId");
        Optional<User> userQueryResult = DataBase.findUserById(userId);

        if (userQueryResult.isEmpty()) {
            return "/user/login_failed.html";
        }
        User user = userQueryResult.get();
        if (!user.matchPassword(requestParam.get("password"))) {
            return "/user/login_failed.html";
        }

        response.setCookie(new Cookie(Cookie.SESSION_ID_NAME, UUID.randomUUID().toString()));
        response.setCookie(new Cookie(Cookie.PATH_NAME, "/"));
        return "/index.html";
    }
}
