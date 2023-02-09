package webserver;

import type.ContentType;
import type.HttpStatusCode;

public class ResponseHeader {
    private static final String PROTOCOL = "HTTP";
    private static final String VERSION = "1.1";
    private final HttpStatusCode httpStatusCode;
    private ContentType contentType;
    private Integer contentLength;
    private String location;
    private String cookie;

    public ResponseHeader(HttpStatusCode httpStatusCode, ContentType contentType, Integer contentLength,
                          String location, String cookie) {
        this.httpStatusCode = httpStatusCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.location = location;
        this.cookie = cookie;
    }

    public static ResponseHeader of(HttpStatusCode httpStatusCode, ContentType contentType, Integer contentLength,
                                    String location) {
        return new ResponseHeader(httpStatusCode, contentType, contentLength, location, null);
    }

    public static ResponseHeader of(HttpStatusCode httpStatusCode, ContentType contentType, Integer contentLength) {
        return new ResponseHeader(httpStatusCode, contentType, contentLength, null, null);
    }

    public static ResponseHeader of(HttpStatusCode httpStatusCode, ContentType contentType) {
        return new ResponseHeader(httpStatusCode, contentType, null, null, null);
    }

    public static ResponseHeader of(HttpStatusCode httpStatusCode, String location) {
        return new ResponseHeader(httpStatusCode, null, null, location, null);
    }

    public static ResponseHeader of(HttpStatusCode httpStatusCode) {
        return new ResponseHeader(httpStatusCode, null, null, null, null);
    }

    public String getValue() {
        String result = writeRequestLine(httpStatusCode);
        if (cookie != null) {
            result += "Set-Cookie: " + cookie + " \r\n";
        }
        if (contentType != null) {
            result += "Content-Type: " + contentType.getToResponseText() + " \r\n";
        }
        if (contentLength != null) {
            result += "Content-Length: " + contentLength + " \r\n";
        }
        if (location != null) {
            result += "Location: " + location + " \r\n";
        }
        result += "\r\n"; // header의 끝은 줄바꿈으로 표시한다

        return result;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    private String writeRequestLine(HttpStatusCode httpStatusCode) {
        return PROTOCOL + "/" + VERSION + " "
                + httpStatusCode.getCode() + " "
                + httpStatusCode.getDescription() + " \r\n";
    }

}
