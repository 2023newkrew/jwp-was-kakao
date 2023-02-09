package webserver.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.controller.annotation.Handler;
import webserver.controller.annotation.RequestController;
import webserver.request.Request;
import webserver.request.support.FormData;
import webserver.request.support.QueryParameters;
import webserver.response.Response;

@RequestController
public class UserController {
    @Handler(method = HttpMethod.GET, value = "/user/create")
    public void createUserGet(Request req, Response res) {
        QueryParameters parameters = new QueryParameters(req.getURL());
        User user = new User(
                parameters.getValue("userId"),
                parameters.getValue("password"),
                parameters.getValue("name"),
                parameters.getValue("email")
        );
        DataBase.addUser(user);
        res.setStatus(HttpStatus.OK);
    }

    @Handler(method = HttpMethod.POST, value = "/user/create")
    public void createUserPost(Request req, Response res) {
        FormData formData = new FormData(req.getBody());
        User user = new User(
                formData.getValue("userId"),
                formData.getValue("password"),
                formData.getValue("name"),
                formData.getValue("email")
        );
        DataBase.addUser(user);
        res.setStatus(HttpStatus.FOUND);
        res.setLocation("http://localhost:8080/index.html");
    }
}
