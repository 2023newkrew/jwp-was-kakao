package supports;

import webserver.RequestHandler;
import utils.LogicValidatorUtils;

import java.util.HashMap;
import java.util.Objects;

public class HttpParser {
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String EQUAL = "=";

    private final HashMap<String, String> httpHeaderInfo = new HashMap<>();

    public HttpParser(String httpRequest) {
        LogicValidatorUtils.checkNull(httpRequest);
        String[] httpInfo = httpRequest.split(NEW_LINE);
        LogicValidatorUtils.checkNull(httpInfo[0]);
        String[] firstLine = httpInfo[0].split(SPACE);

        httpHeaderInfo.put("Http-Method", firstLine[0]);
        httpHeaderInfo.put("Path", firstLine[1]);
        httpHeaderInfo.put("Http-Version", firstLine[2]);

        for (int i = 1; i < httpInfo.length; i++) {
            LogicValidatorUtils.checkNull(httpInfo[i]);
            String[] splittedLine = httpInfo[i].split(COLON + SPACE);
            httpHeaderInfo.put(splittedLine[0], splittedLine[1]);
        }
    }

    public String getCookie() {
        String cookieHeader = httpHeaderInfo.get("Cookie");
        if (cookieHeader == null){
            return null;
        }
        return cookieHeader.split(EQUAL)[1];
    }

    public String getMethod() {
        return httpHeaderInfo.get("Http-Method");
    }

    public String getPath() {
        return httpHeaderInfo.get("Path");
    }

    public Integer getContentLength(){
        return Integer.parseInt(httpHeaderInfo.get("Content-Length"));
    }
}