package webserver.handler;

import db.DataBase;
import model.User;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;

public class CreateUserHandler implements Handler {

    private final String REDIRECT_LOCATION = "/index.html";

    @Override
    public Response apply(Request request) {
        Map<String, String> queryStringMap = request.getRequestBodyAsQueryString();
        User user = User.builder()
                .userId(queryStringMap.get("userId"))
                .password(queryStringMap.get("password"))
                .name(queryStringMap.get("name"))
                .email(queryStringMap.get("email"))
                .build();
        DataBase.addUser(user);
        return Response.found(new byte[0], request.getRequestFileType(), REDIRECT_LOCATION);
    }
}
