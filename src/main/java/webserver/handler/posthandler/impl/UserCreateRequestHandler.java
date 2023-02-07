package webserver.handler.posthandler.impl;

import db.DataBase;
import model.User;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.posthandler.PostRequestHandler;
import webserver.request.HttpRequest;
import webserver.request.QueryParams;
import webserver.response.HttpResponse;

public class UserCreateRequestHandler extends PostRequestHandler {

    private UserCreateRequestHandler() {
    }

    private static class UserPostRequestHandlerHolder {
        public static final UserCreateRequestHandler INSTANCE = new UserCreateRequestHandler();
    }

    public static UserCreateRequestHandler getInstance() {
        return UserCreateRequestHandler.UserPostRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        String path = request.getTarget()
                .getPath();
        return super.canHandle(request) &&
                path.equals("/user/create");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        QueryParams queryParams = new QueryParams(request.getBody());
        addUser(queryParams);
        return new HttpResponse.Builder()
                .setStatus(HttpStatus.FOUND)
                .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/index.html")
                .build();
    }

    private void addUser(QueryParams queryParams) {
        String userId = queryParams.get("userId");
        String password = queryParams.get("password");
        String name = queryParams.get("name");
        String email = queryParams.get("email");
        User user = User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
        DataBase.addUser(user);
    }
}
