package webserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private final String userId;
    private final String password;

    private LoginRequest(String userId, String password) {
        if (userId == null || password == null ||
                userId.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.userId = userId;
        this.password = password;
    }

    public static LoginRequest of(Map<String, String> queryString) {
        return LoginRequest.builder()
                .userId(queryString.get(USER_ID))
                .password(queryString.get(PASSWORD))
                .build();
    }

}
