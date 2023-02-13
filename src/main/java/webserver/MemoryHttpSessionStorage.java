package webserver;

import java.util.Map;

public class MemoryHttpSessionStorage implements SessionStorage {

    private final Map<String, HttpSession> sessionMap;

    public MemoryHttpSessionStorage(Map<String, HttpSession> sessionMap) {
        this.sessionMap = sessionMap;
    }

    @Override
    public HttpSession createHttpSession() {
        HttpSession httpSession = new HttpSession();
        sessionMap.put(httpSession.getId(), httpSession);
        return httpSession;
    }

    @Override
    public HttpSession getSession(String id) {
        return sessionMap.get(id);
    }

    @Override
    public void removeSession(String id) {
        sessionMap.remove(id);
    }
}
