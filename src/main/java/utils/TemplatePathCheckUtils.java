package utils;

import supports.HttpParser;

public class TemplatePathCheckUtils {
    private static final String USER_LIST_URL = "/user/list";
    private static final String USER_PROFILE_URL = "/user/profile";
    private static final String USER_LOGIN_PATH = "/user/login.html";

    public static boolean isLoginAndValidSession(HttpParser httpParser) {
        return httpParser.getPath().startsWith(USER_LOGIN_PATH) && !AuthUtils.checkInvalidSession(httpParser.getCookie());
    }

    public static boolean isProfile(HttpParser httpParser) {
        return httpParser.getPath().startsWith(USER_PROFILE_URL);
    }

    public static boolean isUserList(HttpParser httpParser) {
        return httpParser.getPath().startsWith(USER_LIST_URL);
    }
}
