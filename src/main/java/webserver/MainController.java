package webserver;

import db.DataBase;
import model.User;
import type.ContentType;

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
        String header = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: " + ContentType.HTML.getString() + " \r\n";
        String body = "Hello world";

        return new Response(header, body);
    }

    private Response handleUserCreate(Map<String, String> params) {
        User user = User.from(params);
        DataBase.addUser(user);

        String header = "HTTP/1.1 302 Redirect \r\n" +
                "Location: /index.html \r\n";

        return new Response(header);
    }
}
