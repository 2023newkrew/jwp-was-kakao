package controller;

import db.DataBase;
import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;

public class UserController {

    private static UserController INSTANCE;

    private UserController() { }
    public static UserController getInstance() {
        if(INSTANCE == null)
            INSTANCE = new UserController();
        return INSTANCE;
    }

    public HttpResponse createUserGet(HttpRequest request) {
        User user = new User(
                request.getParam("userId").orElseThrow(),
                request.getParam("password").orElseThrow(),
                request.getParam("name").orElseThrow(),
                request.getParam("email").orElseThrow()
        );

        DataBase.addUser(user);

        return new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8")
                .build();
    }

    public HttpResponse createUserPost(HttpRequest request) {
        User user = new User(
                request.getParam("userId").orElseThrow(),
                request.getParam("password").orElseThrow(),
                request.getParam("name").orElseThrow(),
                request.getParam("email").orElseThrow()
        );

        DataBase.addUser(user);

        return new HttpResponse.Builder()
                .status(HttpStatus.FOUND)
                .addAttribute(HttpHeaders.LOCATION, "/index.html")
                .build();
    }
}
