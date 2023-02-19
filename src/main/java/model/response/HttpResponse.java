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

import java.util.Map;
import java.util.Set;

import static constant.HeaderConstant.*;

@AllArgsConstructor
@Getter
@Builder
public class HttpResponse {
    @Builder.Default
    private ResponseHeader header = new ResponseHeader();
    private HttpStatus status;
    @Builder.Default
    private ResponseBody body = new ResponseBody();

    public boolean isBodyExists() {
        return !body.isEmpty();
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

    public void addCookie(Cookie cookie, Object object) {
        setHeaderAttribute(SET_COOKIE, cookie.toString());
        SessionManager.setCookieInSession(cookie, new UserSession(), object);
    }

}
