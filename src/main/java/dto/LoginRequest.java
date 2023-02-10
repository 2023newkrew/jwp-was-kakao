package dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private final String userId;
    private final String password;

    public static LoginRequest of(Map<String, String> queryString) {
        if (!queryString.containsKey(USER_ID) || !queryString.containsKey(PASSWORD)) {
            throw new IllegalArgumentException();
        }
        return LoginRequest.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .build();
    }

}
