package utils;

import lombok.experimental.UtilityClass;
import webserver.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static utils.QueryStringParser.*;

@UtilityClass
public class RequestBuilder {
    public HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = "";
        String requestPath = "";
        Map<String, String> queryString = new HashMap<>();
        String httpMethod = "";
        String httpProtocol = "";
        Map<String, String> body = new HashMap<>();

        Map<String, String> requestHeaderMap = new HashMap<>();

        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            String[] tokens = line.split(" ");

            if (!line.contains(":")) {
                httpMethod = tokens[0];
                requestPath = tokens[1].split("\\?")[0];

                if (tokens[1].contains("?")) {
                    queryString = parseQueryString(tokens[1].split("\\?")[1]);
                }

                httpProtocol = tokens[2];

                continue;
            }
            requestHeaderMap.put(tokens[0].substring(0, tokens[0].length() - 1), tokens[1]);
        }
        int contentLength = Integer.parseInt(requestHeaderMap.getOrDefault("Content-Length", "0"));
        if (contentLength > 0) {
            body = parseQueryString(IOUtils.readData(bufferedReader, contentLength));
        }
//        if (requestHeaderMap.get)
//        String requestBody = IOUtils.readData(bufferedReader, Integer.parseInt(requestHeaderMap.getOrDefault("Content-Length", "0")));
//        body = parseQueryString(requestBody);

        return new HttpRequest(httpMethod, requestPath, queryString, httpProtocol, requestHeaderMap, body);
    }
}
