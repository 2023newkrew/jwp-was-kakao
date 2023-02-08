package app.user.dto;

import app.user.User;

public class UserResponseDto {

    private final String userId;
    private final String name;
    private final String email;

    public UserResponseDto(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
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

    public static UserResponseDto of(User user){
        return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail());
    }
}
