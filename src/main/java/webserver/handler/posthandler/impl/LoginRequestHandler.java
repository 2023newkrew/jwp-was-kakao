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
    public HttpResponse handle(HttpRequest request) {
        return null;
    }
}
