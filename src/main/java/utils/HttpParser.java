package utils;

import java.util.HashMap;

public class HttpParser {
    private final HashMap<String, String> httpHeaderInfo = new HashMap<>();

    public HttpParser(String httpRequest) {
        System.out.println(httpRequest);

        String[] httpInfo = httpRequest.split("\n");
        String[] firstLine = httpInfo[0].split(" ");

        httpHeaderInfo.put("Http-Method", firstLine[0]);
        httpHeaderInfo.put("Path", firstLine[1]);
        httpHeaderInfo.put("Http-Version", firstLine[2]);

        for (int i = 1; i < httpInfo.length; i++) {
            String key = httpInfo[i].split(": ")[0];
            String value = httpInfo[i].split(": ")[1];
            httpHeaderInfo.put(key, value);
        }
    }

    public String getHttpMethod() {
        return httpHeaderInfo.get("Http-Method");
    }

    public String getPath() {
        return httpHeaderInfo.get("Path");
    }

    public Integer getContentLength(){
        return Integer.parseInt(httpHeaderInfo.get("Content-Length"));
    }
}