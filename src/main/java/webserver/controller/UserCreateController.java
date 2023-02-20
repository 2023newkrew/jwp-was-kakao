package webserver.controller;

import db.DataBase;
import model.User;
import webserver.request.HttpRequest;
import webserver.request.QueryStringParser;
import webserver.response.HttpResponse;
import webserver.utils.ResponseUtil;

import java.util.Map;

import static webserver.request.HttpRequestMethod.GET;
import static webserver.request.HttpRequestMethod.POST;

public class UserCreateController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        Map<String, String> attributes = QueryStringParser.parseQueryString(QueryStringParser.getQuery(request), "&");

        User user = new User(
                attributes.get("userId"),
                attributes.get("password"),
                attributes.get("name"),
                attributes.get("email")
        );

        DataBase.addUser(user);

        ResponseUtil.response302(response, "/index.html");
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return (request.getMethod() == GET || request.getMethod() == POST)
                && request.getUri().getPath().equals("/user/create");
    }
}
