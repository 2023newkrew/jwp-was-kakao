package dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class LoginDto {
    private final String userId;
    private final String password;

    public static LoginDto of(Map<String, String> loginInfo) {
        return new LoginDto(loginInfo.get("userId"), loginInfo.get("password"));
    }
}
