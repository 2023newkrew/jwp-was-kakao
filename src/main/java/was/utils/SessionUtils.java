package was.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import model.User;
import was.session.Session;
import was.session.SessionManager;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtils {
    public static Session getSession(Map<String, String> headers) {
        return CookieUtils.parseCookies(headers.get("Cookie")).stream()
                .map(SessionManager::findSession)
                .filter(Objects::nonNull).
                findAny().orElse(null);
    }

    public static String createSession(User user) {
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid);
        session.setAttribute("user", user);
        SessionManager.add(session);
        return uuid;
    }
}
