package utils;

import java.util.Objects;

public class MethodAndPathCheckUtils {
    private static final String HTML = "html";
    private static final String CSS = "/css";
    private static final String FONTS = "/fonts";
    private static final String IMAGES = "/images";
    private static final String JS = "/js";
    private static final String USER_CREATE_URL = "/user/create";
    private static final String USER_LOGIN_URL = "/user/login";

    public static boolean isGetAndStatic(String method, String path) {
        return Objects.equals(method, "GET") &&
                path.startsWith(CSS) ||
                path.startsWith(FONTS) ||
                path.startsWith(IMAGES) ||
                path.startsWith(JS);
    }

    public static boolean isPostAndCreate(String method, String path) {
        return Objects.equals(method, "POST") && path.startsWith(USER_CREATE_URL);
    }

    public static boolean isGetAndTemplate(String method, String path) {
        return Objects.equals(method, "GET") && path.endsWith(HTML);
    }

    public static boolean isPostAndLogin(String method, String path) {
        return Objects.equals(method, "POST") && path.startsWith(USER_LOGIN_URL);
    }
}
