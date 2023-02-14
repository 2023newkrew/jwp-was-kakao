package dto;

import model.User;

public class UserDto {
    private final long index;
    private final String userId;
    private final String name;
    private final String email;

    private UserDto(long index, String userId, String name, String email) {
        this.index = index;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserDto from(long index, User user) {
        return new UserDto(index, user.getUserId(), user.getName(), user.getEmail());
    }

    public Long getIndex() {
        return index;
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
