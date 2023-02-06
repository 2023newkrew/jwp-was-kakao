package webserver.controller;

import db.DataBase;
import model.User;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.request.FormData;
import webserver.request.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestController {
    private static final Map<PathMapKey, ControllerMethod> pathMap = new HashMap<>();

    static {
        pathMap.put(PathMapKey.of(HttpMethod.GET, "/"), (req, res) -> {
            byte[] body = "Hello world".getBytes();
            res.setBody(body);
            res.setStatus(HttpStatus.OK);
        });

        pathMap.put(PathMapKey.of(HttpMethod.GET, "/user/create"), (req, res) -> {
            User user = new User(
                    req.getQueryParam("userId"),
                    req.getQueryParam("password"),
                    req.getQueryParam("name"),
                    req.getQueryParam("email")
            );
            DataBase.addUser(user);
            res.setStatus(HttpStatus.OK);
        });

        pathMap.put(PathMapKey.of(HttpMethod.POST, "/user/create"), (req, res) -> {
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
        });
    }

    public static ControllerMethod getMappedMethod(Request request) {
        return pathMap.get(PathMapKey.of(request.getHttpMethod(), request.getPath()));
    }
}
