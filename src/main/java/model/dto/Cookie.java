package model.dto;

import java.util.*;

public class Cookie {

    private Map<String, String> cookies;

    public Cookie() {
        this.cookies = new HashMap<>();
    }

    @Override
    public String toString() {
        if(Objects.isNull(cookies.get("JSESSIONID"))) return "";
        return "JSESSIONID=" + cookies.get("JSESSIONID") +
                "; Path=/";
    }

    public void parseCookie(String line){
        line = line.split(": ")[1];
        String[] tokens = line.split(" ");
        Arrays.stream(tokens)
                .filter(str -> !str.equals("null") && !str.equals("null;"))
                .forEach(str -> cookies.put(str.split("=")[0], str.split("=")[1]));
    }

    public void generateUUID(){
        UUID uuid = UUID.randomUUID();
        cookies.put("JSESSIONID", uuid.toString());
    }
}
