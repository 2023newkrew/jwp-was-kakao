package application.dto;

import application.domain.User;

public class UserResponse {
    private final String userId;
    private final String name;
    private final String email;

    private UserResponse(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getName(), user.getEmail());
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
