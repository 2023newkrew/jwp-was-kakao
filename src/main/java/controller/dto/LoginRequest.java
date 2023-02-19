package controller.dto;

import utils.ValidationUtils;

import java.util.Map;

public class LoginRequest {
    private final String userId;
    private final String password;

    public static LoginRequest from(Map<String, String> params) {
        validate(params);
        return new LoginRequest(params.get("userId"), params.get("password"));
    }

    private static void validate(Map<String, String> params) {
        ValidationUtils.checkNotNullAndBlank(params, "userId");
        ValidationUtils.checkNotNullAndBlank(params, "password");
        ValidationUtils.checkSize(params, 2);
    }

    private LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
