package infra.utils.request;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class MyHeaders extends MyRequestMap{
    @Override
    public String get(String key) {
        if(key.equals(HttpHeaders.ACCEPT)){
            String value = super.getOrDefault(key, MediaType.TEXT_HTML_VALUE);
            return value.split(",")[0];
        }
        return super.get(key);
    }

}
