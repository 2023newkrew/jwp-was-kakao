package model.web;

import constant.HeaderConstant;
import lombok.experimental.UtilityClass;
import model.request.HttpRequest;
import model.response.HttpResponse;
import utils.builder.CookieBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static constant.HeaderConstant.*;

@UtilityClass
public class SessionManager {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void add(String id, final Session session) {
        sessions.put(id, session);
    }

    public Session findSession(final String id) {
        try {
            return sessions.get(id);
        } catch (Exception e) {
            throw new RuntimeException("해당 세션이 존재하지 않습니다.");
        }
    }

    public void remove(final String id) {
        sessions.remove(id);
    }

}
