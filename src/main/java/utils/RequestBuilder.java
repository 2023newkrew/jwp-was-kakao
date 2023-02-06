package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import model.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static constant.RequestHeaderConstant.*;
import static java.lang.Integer.*;
import static utils.QueryStringParser.*;

@UtilityClass
public class RequestBuilder {
    public HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = "";
        String requestPath = "";
        Map<String, String> queryParamsMap = new HashMap<>();
        String httpMethod = "";
        String httpProtocol = "";
        Map<String, String> body = new HashMap<>();

        Map<String, String> requestHeaderMap = new HashMap<>();

        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            String[] tokens = line.split(" ");

            if (isFirstLine(line)) {
                httpMethod = tokens[0];
                requestPath = tokens[1].split("\\?")[0];
                httpProtocol = tokens[2];
                queryParamsMap = setQueryParamsMapIfExists(queryParamsMap, tokens);
                continue;
            }

            requestHeaderMap.put(tokens[0].substring(0, tokens[0].length() - 1), tokens[1]);
        }

        if (requestHeaderMap.containsKey(CONTENT_LENGTH)) {
            String requestBody = IOUtils.readData(bufferedReader, parseInt(requestHeaderMap.get(CONTENT_LENGTH)));
            body = getBody(requestBody);
        }

        return new HttpRequest(httpMethod, requestPath, queryParamsMap, httpProtocol, requestHeaderMap, body);
    }

    private Map<String, String> getBody(String requestBody) throws JsonProcessingException {
        if (isJson(requestBody)) {
            return ObjectMapperFactory.getInstance().readValue(requestBody, Map.class);
        }

        return QueryStringParser.parseQueryString(requestBody);
    }

    private boolean isJson(String body) {
        try {
            new JSONObject(body);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    private Map<String, String> setQueryParamsMapIfExists(Map<String, String> queryString, String[] tokens) {
        if (isPathHasQueryString(tokens)) {
            queryString = parseQueryString(tokens[1].split("\\?")[1]);
        }
        return queryString;
    }

    private boolean isPathHasQueryString(String[] tokens) {
        return tokens[1].contains("?");
    }

    private boolean isFirstLine(String line) {
        return !line.contains(":");
    }
}
