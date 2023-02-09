package logics.Service;

import utils.session.Session;
import utils.session.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SessionService has business logic related to Session Management using utils.session.*
 */
public class SessionService {
    public static final SessionService instance = new SessionService();
    private SessionService(){}
    private final UserService userService = UserService.instance;

    /**
     * Procced login with given body received from login-form.
     * @throws IllegalArgumentException if given body is not valid or userID and password not match.
     * @param body should be submitted from login form.
     * @return Session if login success.
     */
    public Session proceedLogin(String body){
        try{
            Map<String, String> bodyMap = parseBody(body);
            if (userService.verifyLogin(bodyMap)){
                return makeSession();
            }
            throw new IllegalArgumentException("ID and password not match!");
        } catch(IndexOutOfBoundsException e){
            throw new IllegalArgumentException("body is not valid");
        }
    }

    private Session makeSession(){
        Session newSession = new Session.Builder().setRandomId().build();
        SessionManager.instance.add(newSession);
        return newSession;
    }

    /**
     * Parse session ID from header value.
     * @param headerParameter to be headerParameter from header, parameter "Cookie"
     * @return Session ID parsed from input. if cannot parse, return null.
     */
    public String parseSessionId (String headerParameter){
        try {
            String[] parsedValue = headerParameter.split(";");
            for (String v : parsedValue){
                if (v.trim().startsWith("JSESSIONID") && v.trim().split("=").length == 2){
                    return v.trim().split("=")[1];
                }
            }
            return null;
        } catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Check validity of given sessionId.
     * @param sessionId to be judged whether valid or not.
     * @return true if valid, else false.
     */
    public boolean isValidSessionId (String sessionId){
        if (Objects.isNull(sessionId) || Objects.isNull(SessionManager.instance.findSession(sessionId))) {
            return false;
        }
        return true;
    }

    private Map<String, String> parseBody(String body){
        Map<String, String> bodyMap = new HashMap<>();
        for (String splitted : body.split("&")) {
            bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
        }
        return bodyMap;
    }
}
