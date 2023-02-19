package session;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {
    private final Map<String, String> cookieMap = new HashMap<>();

    public HttpCookie(String cookieStr){
        for(String keyValuePairStr : cookieStr.split("; ")){
            String[] splited = keyValuePairStr.split("=");
            if(splited.length == 2){
                cookieMap.put(splited[0], splited[1]);
            }
        }
    }

    public String getCookieValueByKey(String key){
        return cookieMap.get(key);
    }
}
