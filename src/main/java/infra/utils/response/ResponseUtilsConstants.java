package infra.utils.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public final class ResponseUtilsConstants {
    private ResponseUtilsConstants() {
    }

    public static final String HTTP_1_1_BASE = "HTTP/1.1 %s \r\n";
    public static final String HTTP_FOUND = String.format(HTTP_1_1_BASE, HttpStatus.FOUND);
    public static final String HTTP_OK = String.format(HTTP_1_1_BASE, HttpStatus.OK);
    public static final String LOCATION = HttpHeaders.LOCATION + ": %s \r\n";
    public static final String SET_COOKIE_JSESSIONID = HttpHeaders.SET_COOKIE + ": JSESSIONID=%s; Path=/ \r\n";
    public static final String TEMPLATES = "templates";
    public static final String STATIC = "static";
    public static final String CONTENT_LENGTH = HttpHeaders.CONTENT_LENGTH + ": %d \r\n";
    public static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE + ": %s;charset=utf-8 \r\n";
    public static final String USER_LIST = "/user/list";
    public static final String HTML_EXTENSION = ".html";

}
