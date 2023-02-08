package auth;

public interface AuthUserDetails {
    String getUserId();
    String getPassword();
    String getName();
    String getEmail();
}
