package utils;

import constant.RequestHeaderConstant;
import lombok.experimental.UtilityClass;
import webserver.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static constant.RequestHeaderConstant.*;
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
        int contentLength = Integer.parseInt(requestHeaderMap.getOrDefault(CONTENT_LENGTH, "0"));

        if (requestHeaderMap.containsKey(CONTENT_LENGTH)) {
            body = parseQueryString(IOUtils.readData(bufferedReader, contentLength));
        }

        return new HttpRequest(httpMethod, requestPath, queryString, httpProtocol, requestHeaderMap, body);
    }
}
