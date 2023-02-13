package webserver;

import type.ContentType;
import type.HttpStatusCode;

public class ResponseHeader {
    private static final String PROTOCOL = "HTTP";
    private static final String VERSION = "1.1";
    private HttpStatusCode httpStatusCode;
    private ContentType contentType;
    private Integer contentLength;
    private String location;
    private HttpCookie httpCookie = new HttpCookie();

    public ResponseHeader() {
        httpStatusCode = HttpStatusCode.OK;
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

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHttpCookie(HttpCookie httpCookie) {
        this.httpCookie = httpCookie;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void putCookieItem(String key, String value) {
        httpCookie.put(key, value);
    }

    private String writeRequestLine(HttpStatusCode httpStatusCode) {
        return PROTOCOL + "/" + VERSION + " "
                + httpStatusCode.getCode() + " "
                + httpStatusCode.getDescription() + " \r\n";
    }

}
