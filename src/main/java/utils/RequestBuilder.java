package utils;

import lombok.experimental.UtilityClass;
import webserver.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RequestBuilder {
    public HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = "";
        String requestPath = "";
        String httpMethod = "";
        String httpProtocol = "";

        Map<String, String> requestHeaderMap = new HashMap<>();

        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            String[] tokens = line.split(" ");

            if (!line.contains(":")) {
                httpMethod = tokens[0];
                requestPath = tokens[1];
                httpProtocol = tokens[2];

                continue;
            }
            requestHeaderMap.put(tokens[0].substring(0, tokens[0].length() - 1), tokens[1]);
        }
        return new HttpRequest(httpMethod, requestPath, httpProtocol, requestHeaderMap);
    }
}
