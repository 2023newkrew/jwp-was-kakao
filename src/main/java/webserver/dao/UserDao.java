package webserver.dao;

import db.DataBase;
import model.HttpRequest;
import model.User;
import utils.ObjectMapperFactory;

public class UserDao {
    public void saveUser(HttpRequest request) {
        DataBase.addUser(ObjectMapperFactory
                .getInstance()
                .convertValue(request.getBody(), User.class));
    }
}
