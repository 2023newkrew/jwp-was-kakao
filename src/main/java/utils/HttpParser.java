package utils;

import java.util.Arrays;

public class HttpParser {
    private final String httpRequest;

    public HttpParser(String httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getHttpMethod() {
        return httpRequest.split(" ")[0];
    }

    public String getPath() {
        System.out.println(httpRequest);
//        Arrays.asList(httpRequest.split(" "))
//                .forEach(System.out::println);
        String[] splited = httpRequest.split(" ");
        if(splited.length > 1){
            return splited[1];
        }
        return "";
        //return httpRequest.split(" ")[1];
    }

}