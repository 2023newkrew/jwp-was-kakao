package webserver.session;

import webserver.collection.StringValues;
import webserver.collection.Values;

public class Session {

    private final String id;

    private final Values values;

    public Session(String id) {
        this.id = id;
        this.values = new StringValues();
    }

    public String getId() {
        return id;
    }

    public void setUserId(String userId) {
        values.put("userId", userId);
    }

    public String getUserId() {
        return values.get("userId");
    }
}
