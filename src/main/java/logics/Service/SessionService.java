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
    private final UserService userService = new UserService();
    public Session login(String body){
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
        SessionManager.getManager.add(newSession);
        return newSession;
    }

    public String parseSessionKey(String value){
        try {
            String[] parsedValue = value.split(";");
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

    public boolean isValidSessionKey(String sessionId){
        if (Objects.isNull(sessionId) || Objects.isNull(SessionManager.getManager.findSession(sessionId))) {
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
