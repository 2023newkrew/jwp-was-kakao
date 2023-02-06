package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.enumeration.HttpMethod;
import lombok.experimental.UtilityClass;
import model.request.*;
import model.request.HttpRequest.HttpRequestBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static constant.HeaderConstant.CONTENT_LENGTH;
import static java.lang.Integer.parseInt;
import static utils.QueryStringParser.*;

@UtilityClass
public class RequestBuilder {
    public HttpRequest getHttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = "";
        Map<String, String> requestHeaderMap = new HashMap<>();
        HttpRequestBuilder requestBuilder = HttpRequest.builder();

        while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
            String[] tokens = line.split(" ");
            if (isFirstLine(tokens[0])) {
                setFirstLineProperties(requestBuilder, tokens);
                continue;
            }

            requestHeaderMap.put(tokens[0].substring(0, tokens[0].length() - 1), tokens[1]);
        }

        if (requestHeaderMap.containsKey(CONTENT_LENGTH)) {
            String requestBody = IOUtils.readData(bufferedReader, parseInt(requestHeaderMap.get(CONTENT_LENGTH)));
            requestBuilder.body(getBody(requestBody));
        }

        return requestBuilder.header(RequestHeader.of(requestHeaderMap))
                .build();
    }

    private void setFirstLineProperties(HttpRequestBuilder requestBuilder, String[] tokens) {
        requestBuilder.method(HttpMethod.valueOf(tokens[0]));
        requestBuilder.URL(tokens[1]);
        requestBuilder.protocol(tokens[2]);
        requestBuilder.queryParams(QueryParams.of(getQueryParamsMapIfExists(tokens)));
    }

    private RequestBody getBody(String requestBody) throws JsonProcessingException {
        if (isJson(requestBody)) {
            return RequestBody.of(
                    (ObjectMapperFactory.getInstance()
                    .readValue(requestBody, Map.class)
            ));
        }

        return RequestBody.of(QueryStringParser.parseQueryString(requestBody));
    }

    private boolean isJson(String body) {
        try {
            new JSONObject(body);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    private Map getQueryParamsMapIfExists(String[] tokens) {
        if (isPathHasQueryString(tokens)) {
           return parseQueryString(tokens[1].split("\\?")[1]);
        }
        return Collections.emptyMap();
    }

    private boolean isPathHasQueryString(String[] tokens) {
        return tokens[1].contains("?");
    }

    private boolean isFirstLine(String token) {
        return HttpMethod.isExist(token);
    }
}
