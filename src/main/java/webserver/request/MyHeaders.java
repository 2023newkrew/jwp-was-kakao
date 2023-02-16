package webserver.request;

import lombok.Getter;
import model.dto.Cookie;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MyHeaders {

    private final Map<String, String> headers;
    private Cookie cookie;

    public MyHeaders(){
        headers = new HashMap<>();
        cookie = new Cookie();
    }

    public void put(String key, String value){
        headers.put(key, value);
    }

    public String get(String key){
        return headers.get(key);
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
}
