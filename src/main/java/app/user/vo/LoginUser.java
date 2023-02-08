package app.user.vo;

import auth.AuthUserDetails;

public class LoginUser {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public LoginUser(AuthUserDetails userDetails) {
        this.userId = userDetails.getUserId();
        this.password = userDetails.getPassword();
        this.name = userDetails.getName();
        this.email = userDetails.getEmail();
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

    public String getEmail() {
        return email;
    }
}
