package repository;

public interface SessionRepository {
    void put(String key, String value);

    String get(String key);
}
