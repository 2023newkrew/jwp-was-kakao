package webserver.handler.posthandler.impl;

import db.DataBase;
import model.User;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.posthandler.PostRequestHandler;
import webserver.request.HttpRequest;
import webserver.request.QueryParams;
import webserver.response.HttpResponse;

import java.util.UUID;

public class LoginRequestHandler extends PostRequestHandler {

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
    public HttpResponse handle(HttpRequest request) {
        QueryParams queryParams = new QueryParams(request.getBody());
        String userId = queryParams.get("userId");
        String password = queryParams.get("password");
        User user = DataBase.findUserById(userId);
        if (user.hasSamePassword(password)) {
            return new HttpResponse.Builder()
                    .setStatus(HttpStatus.FOUND)
                    .addHeader("Set-Cookie", UUID.randomUUID()
                            .toString())
                    .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/index.html")
                    .build();
        } else {
            return new HttpResponse.Builder()
                    .setStatus(HttpStatus.FOUND)
                    .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/login_failed.html")
                    .build();
        }
    }
}
