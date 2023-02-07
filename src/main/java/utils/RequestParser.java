package utils;

import infra.http.header.Headers;
import infra.http.body.StringBody;
import infra.http.request.HttpRequest;
import infra.http.request.RequestLine;
import infra.http.request.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    public static HttpRequest parse(BufferedReader br) {
        try {
            RequestLine requestLine = RequestParser.getRequestLine(br);
            Headers headers = RequestParser.getHeaders(br);
            StringBody body = null;
            if (headers.get(Headers.CONTENT_LENGTH) != null) {
                body = RequestParser.getStringBody(br, Integer.parseInt(headers.get(Headers.CONTENT_LENGTH)));
            }
            return new HttpRequest(requestLine, headers, body);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static RequestLine getRequestLine(BufferedReader br) throws IOException, ArrayIndexOutOfBoundsException {
        String line = br.readLine();
        if (line == null) {
            throw new IOException();
        }
        String[] split = line.split(RequestLine.DELIMITER);
        return new RequestLine(RequestMethod.valueOf(split[0]), split[1], split[2]);
    }

    private static Headers getHeaders(BufferedReader br) throws IOException, ArrayIndexOutOfBoundsException {
        Headers headers = new Headers();
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
            String[] split = line.split(Headers.DELIMITER);
            headers.put(split[0], split[1].trim());
            line = br.readLine();
        }
        return headers;
    }

    private static StringBody getStringBody(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        return new StringBody(body);
    }
}
