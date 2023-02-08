package model.dto;

import java.util.UUID;

public class Cookie {

    private final String JSESSIONID;
    private final String Path;

    public Cookie() {
        this.JSESSIONID = UUID.randomUUID().toString();
        this.Path = "/";
    }

    @Override
    public String toString() {
        return "JSESSIONID=" + JSESSIONID +
                "; Path=" + Path;
    }
}
