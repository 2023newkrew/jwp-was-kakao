package webserver.handler.posthandler.impl;

import db.DataBase;
import model.User;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.posthandler.PostRequestHandler;
import webserver.request.HttpRequest;
import webserver.request.QueryParams;
import webserver.response.HttpResponse;

public class LoginRequestHandler extends PostRequestHandler {

    private static final String LOGIN_SUCCESS_URL = "http://localhost:8080/index.html";

    private static final String LOGIN_FAILED_URL = "http://localhost:8080/user/login_failed.html";

    private LoginRequestHandler() {
    }

    private static class LoginRequestHandlerHolder {
        private static final LoginRequestHandler INSTANCE = new LoginRequestHandler();
    }

    public static LoginRequestHandler getInstance() {
        return LoginRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        return super.canHandle(request)
                && request.getTarget()
                .getPath()
                .equals("/user/login");
    }

    @Override
    public void handle(HttpRequest request, HttpResponse.Builder responseBuilder) {
        QueryParams queryParams = new QueryParams(request.getBody());
        String userId = queryParams.get("userId");
        String password = queryParams.get("password");
        User user = DataBase.findUserById(userId);
        String redirectUrl = LOGIN_FAILED_URL;
        if (user != null && user.hasSamePassword(password)) {
            redirectUrl = LOGIN_SUCCESS_URL;
            request.getSession()
                    .setAttribute("user", user);
        }
        responseBuilder.setStatus(HttpStatus.FOUND)
                .addHeader(HttpHeaderProperties.LOCATION.getKey(), redirectUrl);
    }
}
