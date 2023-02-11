package service;

import db.DataBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.User;
import model.UserRequest;
import was.utils.ParamsParser;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    public static Long createUser(String body) {
        return createUser(ParamsParser.from(body).getParams());
    }

    public static Long createUser(Map<String, String> params) {
        UserRequest userRequest = UserRequest.builder()
                .userId(params.get("userId"))
                .password(params.get("password"))
                .name(params.get("name"))
                .email(params.get("email"))
                .build();
        return DataBase.addUser(userRequest).orElseThrow(RuntimeException::new);
    }

    public static Optional<User> getMatchedUser(String body) {
        String userId = ParamsParser.from(body).getParams().get("userId");
        String password = ParamsParser.from(body).getParams().get("password");

        User user = DataBase.findUserByUserId(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));

        if (user.isMatchPassword(password))
            return Optional.of(user);

        return Optional.ofNullable(null);
    }

    public static boolean isValidUser(User user) {
        return DataBase.findUserByUserId(user.getUserId()).isPresent();
    }

    public static Collection<User> findAllUser() {
        return DataBase.findAll();
    }
}
