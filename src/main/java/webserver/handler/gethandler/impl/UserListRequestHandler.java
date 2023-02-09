package webserver.handler.gethandler.impl;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import webserver.constant.HttpHeaderProperties;
import webserver.constant.HttpStatus;
import webserver.handler.gethandler.GetRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class UserListRequestHandler extends GetRequestHandler {

    private UserListRequestHandler() {
    }

    private static class UserListRequestHandlerHolder {
        private static final UserListRequestHandler INSTANCE = new UserListRequestHandler();
    }

    public static UserListRequestHandler getInstance() {
        return UserListRequestHandlerHolder.INSTANCE;
    }

    @Override
    public boolean canHandle(HttpRequest request) {
        return super.canHandle(request)
                && request.getTarget()
                .getPath()
                .equals("/user/list");
    }

    @Override
    public void handle(HttpRequest request, HttpResponse.Builder responseBuilder) {
        try {
            if (!isLoggedIn(request)) {
                responseBuilder.setStatus(HttpStatus.FOUND)
                        .addHeader(HttpHeaderProperties.LOCATION.getKey(), "http://localhost:8080/user/login.html");
                return;
            }
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/templates");
            loader.setSuffix(".html");
            Handlebars handlebars = new Handlebars(loader);

            Template template = handlebars.compile("/user/list");
            String result = template.apply(Map.of("users", DataBase.findAll()));

            responseBuilder.setStatus(HttpStatus.OK)
                    .setBody(result.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLoggedIn(HttpRequest request) {
        return request.getSession()
                .hasAttribute("user");
    }
}
