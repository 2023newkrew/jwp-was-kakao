package model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public boolean isPasswordMatch(String requestPassword) {
        return password.equals(requestPassword);
    }
}
