package webserver.handler.posthandler.impl;

import webserver.handler.posthandler.PostRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

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
    public void handle(HttpRequest request, HttpResponse.Builder responseBuilder) {
//        QueryParams queryParams = new QueryParams(request.getBody());
//        String userId = queryParams.get("userId");
//        String password = queryParams.get("password");
//        User user = DataBase.findUserById(userId);
//        if (user.hasSamePassword(password)) {
//            return new HttpResponse.Builder()
//                    .setStatus(HttpStatus.FOUND)
//                    .addHeader("Set-Cookie", UUID.randomUUID()
//                            .toString())
//                    .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/index.html")
//                    .build();
//        } else {
//            return new HttpResponse.Builder()
//                    .setStatus(HttpStatus.FOUND)
//                    .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/login_failed.html")
//                    .build();
//        }
    }
}
