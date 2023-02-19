package utils.extractor;

import exception.NoSuchCookieException;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CookieExtractor {
    private final String COOKIE_DELIMITER = "; ";
    private final String COOKIE_KEY_VALUE_DELIMITER = "=";

    public String extract(String cookieString, String key) {
        return Arrays.stream(cookieString.split(COOKIE_DELIMITER))
                .map(cookie -> cookie.split(COOKIE_KEY_VALUE_DELIMITER))
                .filter(keyValue -> getKey(keyValue).equals(key))
                .findFirst()
                .orElseThrow(NoSuchCookieException::new)[1];
    }

    private String getKey(String[] keyValue) {
        return keyValue[0];
    }
}
