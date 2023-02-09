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
    private final String userId;
    private final String password;

    public static LoginRequest of(Map<String, String> queryString) {
        return LoginRequest.builder()
                .userId(queryString.get("userId"))
                .password(queryString.get("password"))
                .build();
    }

}
