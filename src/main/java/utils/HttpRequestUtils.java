package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import webserver.session.HttpCookie;
import webserver.session.HttpSession;

public class HttpRequestUtils {
    private static final String COOKIE_SPLIT_DELIMITER = ";";
    private static final String COOKIE_ATTRIBUTE_SPLIT_DELIMITER = "=";

    public static Map<String, String> parseCookie(String cookie) {
        Map<String, String> results = new HashMap<>();
        if (cookie == null) {
            return results;
        }
        String[] parsedCookie = cookie.split(COOKIE_SPLIT_DELIMITER);
        for (String cookieHeader : parsedCookie) {
            String[] splitAttributeString = cookieHeader.split(COOKIE_ATTRIBUTE_SPLIT_DELIMITER);
            results.put(splitAttributeString[0].trim(), splitAttributeString[1].trim());
        }
        return results;
    }

    public static String convertSessionIdToString(String sessionId) {
        return HttpSession.SESSION_ID_NAME + "=" + sessionId;
    }
}
