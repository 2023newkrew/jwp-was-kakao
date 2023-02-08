package utils.utils;

import lombok.experimental.UtilityClass;
import utils.extractor.CookieExtractor;
import model.request.HttpRequest;

import static constant.DefaultConstant.*;
import static constant.HeaderConstant.*;

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
