package utils;

import model.User;
import request.RequestParams;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StringParser {
    public BufferedReader getBufferReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    public DataOutputStream getOutputStream(OutputStream out) {
        return new DataOutputStream(out);
    }

    public RequestParams getRequestParams(String s){
        String[] splitString = s.split(" ");
        if (splitString.length == 3) {
            return new RequestParams(splitString[0], splitString[1], splitString[2], ContentType.setContentType(splitString[1]));
        }
        return null;
    }

    public User getUserInfo(String requestBody) {
        Map<String, String> bodyMap = new HashMap<>();
        for (String splitted : requestBody.split("&")) {
            bodyMap.put(splitted.split("=")[0], splitted.split("=")[1]);
        }
        return new User(bodyMap.get("userId"), bodyMap.get("password"),
                bodyMap.get("name"), bodyMap.get("email"));
    }

    public int getContentLength(String s){
        if (isContentLength(s)){
            return Integer.parseInt(s.split(":")[1].trim());
        }
        return -1;
    }

    private boolean isContentLength(String s){
        return "Content-Length".equals(s.split(":")[0]);
    }

}
