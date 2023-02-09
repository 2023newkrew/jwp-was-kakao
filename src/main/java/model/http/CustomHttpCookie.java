package model.http;

import java.util.UUID;

public class CustomHttpCookie {

    private final String jSessionId;
    private final String path;

    public CustomHttpCookie() {
        this.jSessionId = createJSessionId();
        this.path = "/";
    }

    public String createJSessionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public String getCookie() {
        return "JSESSIONID=" + jSessionId + "; Path=" + path;
    }

}
