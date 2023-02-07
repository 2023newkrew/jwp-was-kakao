package webserver.handler.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.http.Headers;
import webserver.http.request.Request;
import webserver.http.request.path.PathVariables;
import webserver.http.response.Response;
import webserver.http.response.ResponseHeader;

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

        return new Response(new ResponseHeader(HttpStatus.FOUND, createHeaders()));
    }

    private static Headers createHeaders() {
        var headers = new Headers();
        headers.put("Location: /index.html");
        return headers;
    }
}
