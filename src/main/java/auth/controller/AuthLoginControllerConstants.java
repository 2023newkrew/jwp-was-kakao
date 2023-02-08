package auth.controller;

public enum AuthLoginControllerConstants {
    USER_LOGIN_URL("/user/login"),
    INDEX_HTML_URL("/index.html"),
    LOGIN_FAIL_HTML_URL("/user/login_failed.html");
    private final String url;

    AuthLoginControllerConstants(java.lang.String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
