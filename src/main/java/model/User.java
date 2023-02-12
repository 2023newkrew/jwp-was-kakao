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

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    /* HandleBars에서 사용 */
    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    public boolean isCorrectPassword(String password) {
        return Objects.nonNull(password) && password.equals(getPassword());
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
