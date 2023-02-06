package webserver.controller;

import db.DataBase;
import model.User;
import webserver.type.ContentType;
import webserver.parser.Response;

import java.util.Map;

public final class MainController implements Controller {

    public static final MainController INSTANCE = new MainController();

    private MainController() {
    }

    @Override
    public Response handleRequest(String uri, Map<String, String> params) {
        if (uri.equals("/")) {
            return handleRootPage();
        }
        if (uri.equals("/user/create")) {
            return handleUserCreate(params);
        }
        return null;
    }

    private Response handleRootPage() {
        return Response.ok().contentType(ContentType.HTML).body("Hello world").build();
    }

    private Response handleUserCreate(Map<String, String> params) {
        User user = User.from(params);
        DataBase.addUser(user);

        return Response.redirect().location("/index.html").build();
    }
}
