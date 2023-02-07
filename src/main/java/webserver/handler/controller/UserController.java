package webserver.handler.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.request.Request;
import webserver.request.path.PathVariables;
import webserver.response.Response;

public class UserController extends Controller {

    public UserController() {
        methodHandlers.put(HttpMethod.POST, "/user/create", this::createUser);
    }

    private Response createUser(Request request) {
        PathVariables pathVariables = request.getBodyAsPathVariables();
        var user = new User(
                pathVariables.get("userId"),
                pathVariables.get("password"),
                pathVariables.get("name"),
                pathVariables.get("email")
        );
        DataBase.addUser(user);

        return new Response(HttpStatus.FOUND);
    }
}
