package model.http;

import java.util.UUID;

public class CustomHttpCookie {

    private final String jSessionId;
    private final String path;

    public CustomHttpCookie() {
        this.jSessionId = getJSessionId();
        this.path = "/";
    }

    public String getJSessionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String getCookie() {
        return "JSESSIONID=" + jSessionId + "; Path=" + path;
    }

}
