package webserver.handler.request;

import db.DataBase;
import model.User;
import model.HttpRequest;
import webserver.handler.response.PostResponseHandler;
import webserver.handler.response.ResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PostRequestHandler implements RequestMethodHandler {
    private static PostRequestHandler postRequestHandler = new PostRequestHandler();

    private PostRequestHandler() {}

    public static PostRequestHandler getInstance() {
        return postRequestHandler;
    }

    @Override
    public void handle(HttpRequest httpRequest, OutputStream outputStream) throws IOException, URISyntaxException {
        String uri = httpRequest.getUri();
        String body = httpRequest.getBody();

        if (uri.startsWith("/user/create")) {
            Map<String, String> userData = new HashMap<>();

            String[] bodyLine = body.split("&");

            for (String bodyPart : bodyLine) {
                userData.put(bodyPart.split("=")[0], bodyPart.split("=")[1]);
            }

            User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));

            DataBase.addUser(user);
        }

        ResponseHandler responseHandler = PostResponseHandler.getInstance();

        responseHandler.handle(httpRequest, outputStream);
    }
}
