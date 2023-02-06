package model.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MyHeaders {

    private final Map<String, String> headers;

    public MyHeaders(){
        headers = new HashMap<>();
    }

    public void put(String key, String value){
        headers.put(key, value);
    }

    public String get(String key){
        return headers.get(key);
    }

}
