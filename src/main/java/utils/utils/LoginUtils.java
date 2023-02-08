package utils.utils;

import lombok.experimental.UtilityClass;
import model.request.CookieExtractor;
import model.request.HttpRequest;

import static constant.DefaultConstant.DEFAULT_SESSION_ID;
import static constant.HeaderConstant.COOKIE;

@UtilityClass
public class LoginUtils {
    public boolean isLogin(HttpRequest request) {
        try {
            return request.getSession(DEFAULT_SESSION_ID)
                    .getAttribute(extractDefaultSessionIdCookieValue(request))
                    .isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    private String extractDefaultSessionIdCookieValue(HttpRequest request) {
        return CookieExtractor.extract(request.findHeaderValue(COOKIE, null), DEFAULT_SESSION_ID);
    }
}
