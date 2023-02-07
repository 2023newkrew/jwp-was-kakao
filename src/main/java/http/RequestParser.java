package http;

import model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public BufferedReader getBufferReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    public DataOutputStream getOutputStream(OutputStream out) {
        return new DataOutputStream(out);
    }

    public String[] splitSpare(String s){
        return s.split(" ");
    }

    public String[] splitDot(String s){
        return s.split("\\.");
    }

    public boolean isContentLength(String s){
        return "Content-Length".equals(s.split(":")[0]);
    }

    public int getContentLength(String s){
        if (isContentLength(s)){
            return Integer.parseInt(s.split(":")[1].trim());
        }
        return -1;
    }

    public User getUserInfo(String requestBody) {
        Map<String, String> bodyMap = new HashMap<>();
        for (String splitted : requestBody.split("&")) {
            bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
        }
        return new User(bodyMap.get("userId"), bodyMap.get("password"),
                bodyMap.get("name"), bodyMap.get("email"));
    }
}
