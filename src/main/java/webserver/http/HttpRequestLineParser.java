package webserver.http;

import http.exception.HttpRequestFormatException;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestLineParser {

    public String extractHttpMethod(String requestLine) {
        validateRequestLine(requestLine);
        return requestLine.split(" ")[0];
    }

    public String extractUrl(String requestLine) {
        validateRequestLine(requestLine);
        String urlAndParams = requestLine.split(" ")[1];
        return urlAndParams.split("\\?", 2)[0];
    }

    public Map<String, String> extractParams(String requestLine) {
        validateRequestLine(requestLine);

        String url = requestLine.split(" ")[1];
        String[] urlAndParams = url.split("\\?", 2);

        if (urlAndParams.length == 1) {
            return Map.of();
        }

        return extractParamMap(urlAndParams[1]);
    }

    public String extractHttpVersion(String requestLine) {
        validateRequestLine(requestLine);
        return requestLine.split(" ")[2];
    }

    private void validateRequestLine(String requestLine) {
        if (requestLine == null || requestLine.split(" ").length != 3) {
            throw new HttpRequestFormatException();
        }
    }

    private Map<String, String> extractParamMap(String paramString) {
        Map<String, String> paramMap = new HashMap<>();

        String[] splitParam = paramString.split("&");

        Arrays.stream(splitParam)
                .filter(param -> !param.isBlank())
                .map(param -> param.split("=", 2))
                .map(this::convertToEntry)
                .forEach(paramEntry -> paramMap.put(paramEntry.getKey(), paramEntry.getValue()));

        return paramMap;
    }

    private Map.Entry<String, String> convertToEntry(String[] nameAndValue) {
        if (nameAndValue.length == 1) {
            return new SimpleEntry<>(nameAndValue[0], "");
        } else {
            return new SimpleEntry<>(nameAndValue[0], nameAndValue[1]);
        }
    }
}
