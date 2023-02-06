package model.request;

import lombok.Getter;

@Getter
public class RequestURL {
    private final String URL;

    public RequestURL(String URL) {
        this.URL = URL;
    }
}
