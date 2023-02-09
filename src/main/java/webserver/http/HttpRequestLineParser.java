package webserver.http;

import http.HttpMethod;
import http.HttpRequestParams;
import http.exception.HttpRequestFormatException;

public class HttpRequestLineParser {

    public HttpMethod extractHttpMethod(String requestLine) {
        validateRequestLine(requestLine);
        HttpMethod httpMethod = HttpMethod.of(requestLine.split(" ")[0]);
        if (httpMethod == null) {
            throw new HttpRequestFormatException();
        }
        return httpMethod;
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
