package application.controller;

import application.db.DataBase;
import application.model.User;
import org.springframework.http.HttpStatus;
import webserver.handler.controller.AbstractController;
import webserver.http.header.HeaderType;
import webserver.http.header.Headers;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseHeader;

public class UserController extends AbstractController {
    private static final String INDEX_PATH = "/index.html";

    public UserController() {
        addPostHandler("/user/create", this::createUser);
    }

    private Response createUser(Request request) {
        var user = new User(
                request.getBody("userId"),
                request.getBody("password"),
                request.getBody("name"),
                request.getBody("email")
        );
        DataBase.addUser(user);
        ResponseHeader header = new ResponseHeader(HttpStatus.FOUND, createHeaders());

        return new Response(header);
    }

    private Headers createHeaders() {
        var headers = new Headers();
        headers.put(HeaderType.LOCATION, INDEX_PATH);
        
        return headers;
    }
}
