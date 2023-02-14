package webserver.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static webserver.dto.UserConstant.PASSWORD;
import static webserver.dto.UserConstant.USER_ID;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {
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
