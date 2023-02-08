package auth;

import java.util.UUID;

public class AuthUserDetailsWithUuid implements AuthUserDetails {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;
    private final String uuid;

    public AuthUserDetailsWithUuid(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.uuid = UUID.randomUUID().toString();
    }

    public static AuthUserDetailsWithUuid of(AuthUserDetails authUserDetails){
        return new AuthUserDetailsWithUuid(authUserDetails.getUserId(), authUserDetails.getPassword(),
                authUserDetails.getName(), authUserDetails.getEmail());
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "AuthLoginUserDetails{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
