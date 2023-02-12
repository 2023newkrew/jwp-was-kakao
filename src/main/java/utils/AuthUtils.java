package utils;

import auth.HttpCookie;
import auth.Session;
import auth.SessionManager;
import model.User;

import java.util.UUID;

public class AuthUtils {

    public static boolean checkInvalidSession(String cookie){
        return cookie == null || SessionManager.findSession(cookie) == null;
    }

    public static HttpCookie makeHttpCookie(User user) {
        HttpCookie httpCookie = new HttpCookie(UUID.randomUUID());
        Session session = new Session(httpCookie.getCookie());
        session.setAttribute("userObject", user);
        SessionManager.add(httpCookie, session);
        return httpCookie;
    }
}
