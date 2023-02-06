package webserver;

import db.DataBase;
import http.HttpHeaders;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;

public class UserController {
    public HttpResponse createUserGet(HttpRequest request) {
        String query = request.getUri().getQuery();
        User user = User.fromQueryString(query);

        DataBase.addUser(user);

        HttpResponse response = new HttpResponse.Builder()
                .addAttribute(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8")
                .build();

        return response;
    }

    public HttpResponse createUserPost(HttpRequest request) {
        String query = request.getBody();
        User user = User.fromQueryString(query);

        DataBase.addUser(user);

        HttpResponse response = new HttpResponse.Builder()
                .status(HttpStatus.FOUND)
                .addAttribute(HttpHeaders.LOCATION, "/index.html")
                .build();

        return response;
    }
}
