package model;

public class UserDto {
    private final int index;

    private final String userId;
    private final String name;
    private final String email;

    public UserDto(int index, String userId, String name, String email) {
        this.index = index;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserDto of(User user, int index){
        return new UserDto(index, user.getUserId(), user.getName(), user.getEmail());
    }

    public int getIndex() {
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
