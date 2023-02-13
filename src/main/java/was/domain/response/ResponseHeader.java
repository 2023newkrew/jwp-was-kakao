package was.domain.response;

import lombok.Builder;
import was.domain.Cookie;

@Builder
public class ResponseHeader {
    private final Cookie cookie;
    private final String contentType;
    private final Integer contentLength;
    private final String location;

    public String getHeader() {
        return String.join("", getContentType(), getContentLength(), getLocation(), getSetCookie(), "\r\n");
    }

    private String getContentType() {
        if (contentType == null || contentType.isEmpty())
            return "";

        return String.format("Content-Type: %s \r%n", contentType);
    }

    private String getContentLength() {
        if (contentLength == null || contentLength == 0) {
            return "";
        }
        return String.format("Content-Length: %d \r%n", contentLength);
    }

    private String getLocation() {
        if (location == null || location.length() == 0) {
            return "";
        }

        return String.format("Location: %s \r%n", location);
    }

    private String getSetCookie() {
        if (cookie == null) return "";

        return String.format("Set-Cookie: JSESSIONID=%s; path=\"/\" \r%n", cookie.getUuid().toString());
    }
}
