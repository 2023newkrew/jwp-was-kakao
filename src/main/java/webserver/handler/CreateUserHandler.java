package webserver.handler;

import db.DataBase;
import model.User;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;

public class CreateUserHandler implements Handler {
    @Override
    public Response apply(Request request) {
        Map<String, String> requestBody = request.getRequestBody();
        User user = new User(
            requestBody.get("userId"),
            requestBody.get("password"),
            requestBody.get("name"),
            requestBody.get("email")
        );
        DataBase.addUser(user);
        return Response.found(new byte[0], request.findRequestedFileType(), "/index.html");
    }
}
