package utils.builder;

import lombok.experimental.UtilityClass;
import model.web.Cookie;

@UtilityClass
public class CookieBuilder {
    private final String ENTIRE_PATH = "; Path=/";

    public Cookie build(String key, String value) {
        return new Cookie(key, value + ENTIRE_PATH);
    }
}
