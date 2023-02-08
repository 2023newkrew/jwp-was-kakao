package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.DataBase;
import model.User;

import java.util.Map;

public class UserService {

    private static final UserService instance = new UserService();
    private static final ObjectMapper mapper = new ObjectMapper();

    private UserService(){

    }

    public static UserService getInstance() {
        return instance;
    }

    public void createUser(Map<String, String> params) {
        User user = mapper.convertValue(params, User.class);
        DataBase.addUser(user);
    }
}
