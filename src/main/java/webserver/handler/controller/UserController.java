package webserver.handler.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

public class UserController extends Controller {

    public UserController() {
        super("/user");

        methodHandlers.put(HttpMethod.POST, "/create", this::createUser);
    }

    private Response createUser(Request request) {
        var user = new User(
                request.getPathVariable("userId"),
                request.getPathVariable("password"),
                request.getPathVariable("name"),
                request.getPathVariable("email")
        );
        DataBase.addUser(user);

        return new Response(HttpStatus.OK);
    }
}
