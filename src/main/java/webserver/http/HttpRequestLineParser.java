package webserver.http;

import http.HttpRequestParams;
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

    public HttpRequestParams extractParams(String requestLine) {
        validateRequestLine(requestLine);

        String url = requestLine.split(" ")[1];
        String[] urlAndParams = url.split("\\?", 2);

        if (urlAndParams.length == 1) {
            return new HttpRequestParams();
        }

        return new HttpRequestParams(urlAndParams[1]);
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
}
