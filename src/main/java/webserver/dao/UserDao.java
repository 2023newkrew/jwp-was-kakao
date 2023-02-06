package webserver.dao;

import db.DataBase;
import model.request.HttpRequest;
import model.user.User;
import utils.ObjectMapperFactory;

public class UserDao {
    public void saveUser(HttpRequest request) {
        DataBase.addUser(
                ObjectMapperFactory
                        .getInstance()
                        .convertValue(request.getBody().getRequestBody(), User.class)
        );
    }

    public User findUserByUserId(String userId) {
        return DataBase.findUserById(userId);
    }
}
