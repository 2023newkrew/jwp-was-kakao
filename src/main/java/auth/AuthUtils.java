package auth;

public class AuthUtils {

    public static boolean checkInvalidSession(String cookie){
        return cookie == null || SessionManager.findSession(cookie) == null;
    }
}
