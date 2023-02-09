package logics.Service;

import db.DataBase;
import utils.session.Session;
import utils.session.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SessionService {

    public Session login(String body){
        try{
            Map<String, String> bodyMap = parseBody(body);
            return verifyLogin(bodyMap);
        } catch(IndexOutOfBoundsException e){
            throw new IllegalArgumentException("body is not valid");
        }
    }

    private Session verifyLogin(final Map<String, String> bodyMap){
        String userId = bodyMap.get("userId");
        String password = bodyMap.get("password");
        if (Objects.isNull(DataBase.findUserById(userId)) ||
                !DataBase.findUserById(userId).getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid ID or Password");
        }
        return makeSession();
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
