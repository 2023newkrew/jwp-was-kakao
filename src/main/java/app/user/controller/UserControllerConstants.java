package app.user.controller;

public enum UserControllerConstants {
    LOGIN_HTML_URL("/user/login.html"),
    INDEX_HTML_URL("/index.html"),
    USER_FORM_HTML_URL("/user/form.html"),
    USER_CREATE_URL("/user/create"),
    USER_LOGIN_HTML_URL ("/user/login.html"),
    USER_LOGIN_FAIL_HTML_URL ("/user/login_failed.html"),
    USER_LIST_URL ("/user/list"),
    USER_LIST_HTML_URL ("/user/list.html")
    ;

    private final String url;

    UserControllerConstants(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
