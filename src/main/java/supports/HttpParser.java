package supports;

import java.util.HashMap;

public class HttpParser {
    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";
    public static final String COLON = ":";

    private final HashMap<String, String> httpHeaderInfo = new HashMap<>();

    public HttpParser(String httpRequest) {
        String[] httpInfo = httpRequest.split(NEW_LINE);
        String[] firstLine = httpInfo[0].split(SPACE);

        httpHeaderInfo.put("Http-Method", firstLine[0]);
        httpHeaderInfo.put("Path", firstLine[1]);
        httpHeaderInfo.put("Http-Version", firstLine[2]);

        for (int i = 1; i < httpInfo.length; i++) {
            String key = httpInfo[i].split(COLON + SPACE)[0];
            String value = httpInfo[i].split(COLON + SPACE)[1];
            httpHeaderInfo.put(key, value);
        }
    }

    public String getPath() {
        return httpHeaderInfo.get("Path");
    }

    public Integer getContentLength(){
        return Integer.parseInt(httpHeaderInfo.get("Content-Length"));
    }
}