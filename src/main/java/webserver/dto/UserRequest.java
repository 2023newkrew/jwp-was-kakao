package webserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import webserver.model.User;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserRequest {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public static UserRequest of(Map<String, String> queryStringMap) {
        return UserRequest.builder()
                .userId(queryStringMap.get("userId"))
                .password(queryStringMap.get("password"))
                .name(queryStringMap.get("name"))
                .email(queryStringMap.get("email"))
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
