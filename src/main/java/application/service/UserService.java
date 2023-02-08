package application.service;

import application.db.DataBase;
import application.domain.User;
import application.dto.UserResponse;
import application.utils.SecurityUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public UserResponse save(Map<String, String> userInfo) {
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        DataBase.addUser(user);
        return UserResponse.from(user);
    }

    public Optional<UserResponse> login(Map<String, String> userInfo) {
        User user = DataBase.findUserById(userInfo.get("userId"));

        if (user != null && user.getPassword().equals(userInfo.get("password"))) {
            return Optional.of(UserResponse.from(user));
        }

        return Optional.empty();
    }

    public List<UserResponse> findAllUsers() {
        return DataBase.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }
}
