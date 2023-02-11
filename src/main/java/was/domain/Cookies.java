package was.domain;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cookies {
    private static final Map<String, Cookie> SAVED_COOKIES = new HashMap<>();

    private Cookies() {
    }

    public static Cookie createCookie(User user) {
        UUID uuid = UUID.randomUUID();
        String path = "/";
        Cookie newCookie = new Cookie(uuid, path, user);
        SAVED_COOKIES.put(uuid.toString(), newCookie);

        return newCookie;
    }

    public static Cookie getCookie(String sessionId) {
        return SAVED_COOKIES.getOrDefault(sessionId, null);
    }
}
