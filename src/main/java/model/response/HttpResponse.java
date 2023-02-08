package model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import model.enumeration.HttpStatus;
import model.response.properties.ResponseBody;
import model.response.properties.ResponseHeader;
import model.web.Cookie;
import model.web.SessionManager;
import model.web.UserSession;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static constant.HeaderConstant.*;

@AllArgsConstructor
@Getter
@Builder
public class HttpResponse {
    private final String COOKIE_PATH_DELIMITER = ";";
    @Builder.Default
    private ResponseHeader header = ResponseHeader.of(new LinkedHashMap<>());
    private HttpStatus status;
    @Builder.Default
    private ResponseBody body = new ResponseBody();

    public boolean isBodyExists() {
        return !body.isEmpty();
    }

    public String findHeaderValue(String key) {
        return header.getHeaders().get(key);
    }

    public void setHeaderAttribute(String key, String value) {
        header.setAttribute(key, value);
    }


    public byte[] getBody() {
        return body.get();
    }

    public int getBodyLength() {
        return body.length();
    }

    public Set<Map.Entry<String, String>> getHeaderEntrySet() {
        return header.getEntrySet();
    }

    public String getStatusLine() {
        return status.getStatusLine();
    }

    public void addCookieAndSession(Cookie cookie, Object object) {
        setHeaderAttribute(SET_COOKIE, cookie.toString());
        SessionManager.add(cookie.key(), new UserSession());
        SessionManager.findSession(cookie.key()).setAttribute(getCookieValue(cookie), object);
    }

    private String getCookieValue(Cookie cookie) {
        return cookie.value().split(COOKIE_PATH_DELIMITER)[0];
    }
}
