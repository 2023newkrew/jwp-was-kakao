package constant;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE;

    public static boolean isExist(String string) {
        try {
            HttpMethod.valueOf(string);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
