package webserver;

import type.ContentType;
import type.HttpStatusCode;

import java.util.Set;

public class ResponseHeader {
    private static final String PROTOCOL = "HTTP";
    private static final String VERSION = "1.1";
    private final HttpStatusCode httpStatusCode;
    private ContentType contentType;
    private Integer contentLength;
    private String location;
    private HttpCookie httpCookie;

    public ResponseHeader(HttpStatusCode httpStatusCode, ContentType contentType, Integer contentLength,
                          String location, HttpCookie httpCookie) {
        this.httpStatusCode = httpStatusCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.location = location;
        this.httpCookie = httpCookie;
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
        StringBuilder sb = new StringBuilder();
        sb.append(writeRequestLine(httpStatusCode));
        if (httpCookie != null) {
            sb.append(httpCookie.parseString());
        }
        if (contentType != null) {
            sb.append("Content-Type: ").append(contentType.getToResponseText()).append(" \r\n");
        }
        if (contentLength != null) {
            sb.append("Content-Length: ").append(contentLength).append(" \r\n");
        }
        if (location != null) {
            sb.append("Location: ").append(location).append(" \r\n");
        }
        sb.append("\r\n"); // header의 끝은 줄바꿈으로 표시한다

        return sb.toString();
    }

    public void setCookie(HttpCookie httpCookie) {
        this.httpCookie = httpCookie;
    }

    private String writeRequestLine(HttpStatusCode httpStatusCode) {
        return PROTOCOL + "/" + VERSION + " "
                + httpStatusCode.getCode() + " "
                + httpStatusCode.getDescription() + " \r\n";
    }

}
