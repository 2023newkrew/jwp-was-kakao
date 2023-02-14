package webserver.repository;

import webserver.model.Session;

public interface SessionRepository {
    void save(Session session);

    Session findById(String id);

    boolean isExist(String id);

    void remove(String id);
}
