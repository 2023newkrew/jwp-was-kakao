package webserver.http;

import http.HttpRequestParams;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class HttpRequestParamParser {
    public HttpRequestParams parse(String parameters) {
        HttpRequestParams params = new HttpRequestParams();
        String[] splitParam = parameters.split("&");

        Arrays.stream(splitParam)
                .filter(param -> !param.isBlank())
                .map(param -> param.split("=", 2))
                .map(this::convertToEntry)
                .forEach(paramEntry -> params.setParameter(paramEntry.getKey(), paramEntry.getValue()));

        return params;
    }

    private Map.Entry<String, String> convertToEntry(String[] nameAndValue) {
        if (nameAndValue.length == 1) {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], "");
        } else {
            return new AbstractMap.SimpleEntry<>(nameAndValue[0], nameAndValue[1]);
        }
    }
}
