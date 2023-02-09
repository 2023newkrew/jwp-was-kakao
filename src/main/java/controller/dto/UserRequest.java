package controller.dto;

import utils.ValidationUtils;

import java.util.Map;

public class UserRequest {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public static UserRequest from(Map<String, String> params) {
        validate(params);
        return new UserRequest(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
    }

    private static void validate(Map<String, String> params) {
        ValidationUtils.checkNotNullAndBlank(params, "userId");
        ValidationUtils.checkNotNullAndBlank(params, "password");
        ValidationUtils.checkNotNullAndBlank(params, "name");
        ValidationUtils.checkNotNullAndBlank(params, "email");
    }

    private UserRequest(String userId, String password, String name, String email) {
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

    public String getEmail() {
        return email;
    }
}
