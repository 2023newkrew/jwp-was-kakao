package webserver.request;

public class RequestConstant {
    // String Constant
    public static final String WHITE_SPACE_REGEX = " ";
    public static final String PERIOD_REGEX = "\\.";
    public static final String HEADER_KEY_SEPARATOR = ":";
    public static final String QUERY_STRING_IDENTIFIER = "\\?";
    public static final String QUERY_STRING_CONNECTOR = "&";
    public static final String QUERY_STRING_SEPARATOR = "=";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String COOKIE = "Cookie";
    public static final String SESSION_KEY = "JSESSIONID";

    // Number Constant
    public static final int METHOD_INDEX = 0;
    public static final int URL_INDEX = 1;
    public static final int PROTOCOL_INDEX = 2;
    public static final int PATH_INDEX = 0;
    public static final int QUERY_STRING_INDEX = 1;
    public static final int KEY_INDEX = 0;
    public static final int VALUE_INDEX = 1;

}
