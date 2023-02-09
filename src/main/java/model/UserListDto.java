package model;

/**
 * UserListDto excludes some sensitive information such as password,
 * and adds index number to be shown on frontend.
 */
public class UserListDto {
    private final int index;

    private final String userId;
    private final String name;
    private final String email;

    public UserListDto(int index, String userId, String name, String email) {
        this.index = index;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public static UserListDto of(User user, int index){
        return new UserListDto(index, user.getUserId(), user.getName(), user.getEmail());
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
