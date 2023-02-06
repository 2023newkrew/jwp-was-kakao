package utils;

import model.User;
import model.dto.MyParams;

public class UserFactory {

    private UserFactory(){

    }

    public static User createUser(MyParams userData){
        return new User(userData.get("userId"),
                userData.get("password"),
                userData.get("name"),
                userData.get("email"));
    }
}
