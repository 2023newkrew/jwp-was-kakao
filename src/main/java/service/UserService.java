package service;

import db.DataBase;
import dto.UserLoginDto;
import model.User;

public class UserService {
    private static final UserService INSTANCE = new UserService();

    private UserService(){}

    public static UserService getInstance(){
        return INSTANCE;
    }

    public boolean login(UserLoginDto userLoginDto) {
        String userId = userLoginDto.getUserId();
        User user = DataBase.findUserById(userId);

        return user.matchPassword(userLoginDto.getPassword());
    }

    public void createUser(User user) {
        DataBase.addUser(user);
    }
}
