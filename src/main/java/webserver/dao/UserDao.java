package webserver.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.DataBase;
import model.User;
import model.HttpRequest;
import utils.ObjectMapperFactory;

import java.util.Map;

public class UserDao {
    public void saveUser(HttpRequest request) {
        DataBase.addUser(ObjectMapperFactory
                        .getInstance()
                        .convertValue(request.getBody(), User.class));
    }
}
