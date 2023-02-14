package webserver.collection;

import java.util.Set;

public interface Values {

    void put(String key, String value);

    String get(String key);

    Set<String> keySet();
}
