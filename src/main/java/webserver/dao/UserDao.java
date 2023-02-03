package webserver.dao;

import db.DataBase;
import model.User;
import model.HttpRequest;

import java.util.Map;

public class UserDao {
    public void saveUser(HttpRequest request) {
        Map<String, String> requestBody = request.getBody();
        DataBase.addUser(new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"))
        );
    }
}
