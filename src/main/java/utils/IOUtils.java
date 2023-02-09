package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import webserver.request.Cookie;
import webserver.request.HttpRequestLine;
import webserver.request.HttpRequest;

public class IOUtils {
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static HttpRequest parseHttpRequest(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        HttpRequestLine httpRequestLine = new HttpRequestLine(line);
        Map<String, String> headers = parseHttpHeaders(bufferedReader);
        Cookie cookie = new Cookie(headers.get("Cookie"));  // Todo : Request 안으로 전부 집어넣기
        if (headers.containsKey("Content-Length")) {
            int length = Integer.parseInt(headers.get("Content-Length"));
            return new HttpRequest(httpRequestLine, headers, cookie, readData(bufferedReader, length));
        }
        return new HttpRequest(httpRequestLine, headers, cookie);
    }

    private static Map<String, String> parseHttpHeaders(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> headers = new HashMap<>();
        while (!isNullOrEmpty(line)) {
            String[] tokens = line.split(":");
            headers.put(tokens[0], tokens[1].trim());
            line = bufferedReader.readLine();
        }
        return headers;
    }

    private static boolean isNullOrEmpty(String line) {
        return line == null || "".equals(line);
    }
}
