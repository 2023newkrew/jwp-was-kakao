package webserver;

public interface SessionStorage {
    HttpSession createHttpSession();
    HttpSession getSession(String id);
    void removeSession(String id);
}
