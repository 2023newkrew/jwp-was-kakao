package auth;

import supports.HttpParser;

public class LoginChecker {
    public static boolean check(HttpParser httpParser){
        String cookie = httpParser.getCookie();
        if (cookie == null){
            return false;
        }

        Session session = SessionManager.findSession(cookie);
        if (session == null){
            return false;
        }

        return true;
    }
}
