package utils;

import infra.http.Headers;
import infra.http.HttpMessageBase;
import infra.http.request.HttpRequest;
import infra.http.request.HttpRequestMethod;
import infra.http.request.RequestLine;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    public static HttpRequest parse(BufferedReader br) {
        try {
            RequestLine requestLine = RequestParser.getRequestLine(br);
            Headers headers = RequestParser.getHeaders(br);
            String body = RequestParser.getBody(br);
            return new HttpRequest(requestLine, headers, body);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static RequestLine getRequestLine(BufferedReader br) throws IOException, ArrayIndexOutOfBoundsException {
        String[] split = br.readLine().split(RequestLine.DELIMITER);
        return new RequestLine(HttpRequestMethod.valueOf(split[0]), split[1], split[2]);
    }

    private static Headers getHeaders(BufferedReader br) throws IOException, ArrayIndexOutOfBoundsException {
        Headers headers = new Headers();
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
            String[] split = line.split(Headers.DELIMITER);
            headers.put(split[0], split[1]);
            line = br.readLine();
        }
        return headers;
    }

    private static String getBody(BufferedReader br) throws IOException {
        StringBuilder body = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            body.append(line);
            body.append(HttpMessageBase.LINE_DELIMITER);
        }
        return body.toString();
    }
}
