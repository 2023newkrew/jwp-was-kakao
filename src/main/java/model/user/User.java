package model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public static User from(Map<String, String> userInfo) {
        return new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
    }
}
