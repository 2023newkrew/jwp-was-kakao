package model.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpResponse {
    private final ResponseHeader header;

    public String getBodyValue(String key) {
        return header.getHeaders().get(key);
    }

    public void setAttribute(String key, String value) {
        header.put(key, value);
    }
}
