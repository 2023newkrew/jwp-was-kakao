package model.request;


import model.enumeration.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static model.request.HttpRequest.HttpRequestBuilder;
import static utils.Constants.CONTENT_LENGTH;

public class HttpRequestFactory {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    public static HttpRequest parse(BufferedReader bufferedReader) {
        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
        Map<String, String> headerMap = new HashMap<>();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                String[] tokens = line.split(" ");
                if (isFirstLine(tokens[0])) {
                    setRequestWithFirstLine(httpRequestBuilder, tokens);
                }
                String key = tokens[0].substring(0, tokens[0].length() - 1);

                headerMap.put(key, tokens[1]);
            }
            httpRequestBuilder.headers(headerMap);
            httpRequestBuilder.body(parseBody(bufferedReader, headerMap));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return httpRequestBuilder.build();
    }

    private static void setRequestWithFirstLine(HttpRequestBuilder httpRequestBuilder, String[] tokens) {
        httpRequestBuilder.method(HttpMethod.valueOf(tokens[0]));
        httpRequestBuilder.path(tokens[1]);
        httpRequestBuilder.queryParams(parseQueryParams(tokens[1]));
    }

    private static Map<String, String> parseQueryParams(String urlString) {
        Map<String, String> parameterMap = new HashMap<>();

        if (urlString.contains("?")) {
            String[] queries = urlString
                    .split("\\?")[1]
                    .split("&");

            Arrays.stream(queries)
                    .map(s -> s.split("="))
                    .forEach(query -> parameterMap.put(query[0], query[1]));
        }

        return parameterMap;
    }

    private static Map<String, String> parseBody(BufferedReader bufferedReader, Map<String, String> headerMap) {
        if (!headerMap.containsKey(CONTENT_LENGTH)) {
            return new HashMap<>();
        }

        Map<String, String> requestBody = new HashMap<>();

        try {
            String requestBodyString = IOUtils.readData(bufferedReader, Integer.parseInt(headerMap.get(CONTENT_LENGTH)));
            Arrays.stream(requestBodyString.split("&"))
                    .map(s -> s.split("="))
                    .forEach(keyValueSet -> requestBody.put(keyValueSet[0], keyValueSet[1]));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return requestBody;
    }

    private static boolean isFirstLine(String token) {
        try {
            HttpMethod.valueOf(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
