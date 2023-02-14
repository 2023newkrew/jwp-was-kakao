package webserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import webserver.model.User;

import java.util.Map;

import static webserver.dto.UserConstant.*;

@Builder(access = AccessLevel.PRIVATE)
public class UserRequest {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private UserRequest(String userId, String password, String name, String email) {
        validateArgument(userId, password, name, email);

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validateArgument(String userId, String password, String name, String email) {
        nullValidate(userId, password, name, email);
        emptyValidate(userId, password, name, email);
    }

    private void nullValidate(String userId, String password, String name, String email) {
        if (userId == null || password == null || name == null || email == null) {
            throw new IllegalArgumentException();
        }
    }

    private void emptyValidate(String userId, String password, String name, String email) {
        if (userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public static UserRequest of(Map<String, String> queryStringMap) {
        return UserRequest.builder()
                .userId(queryStringMap.get(USER_ID))
                .password(queryStringMap.get(PASSWORD))
                .name(queryStringMap.get(NAME))
                .email(queryStringMap.get(EMAIL))
                .build();
    }

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
    }
}
