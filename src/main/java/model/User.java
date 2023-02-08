package model;

import java.util.Objects;

public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(final String userId, final String password, final String name, final String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    /* 2단계에서 사용 예정 */
    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        return Objects.nonNull(password) && password.equals(this.password);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
