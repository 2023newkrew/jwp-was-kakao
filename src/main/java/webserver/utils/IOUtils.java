package webserver.utils;

import webserver.enums.RequestMethod;
import webserver.exceptions.InvalidQueryParameterException;
import webserver.exceptions.InvalidRequestException;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class IOUtils {
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static HttpRequest parseRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String[] requestFirstLine = parseRequestFirstLine(bufferedReader);
        RequestMethod requestMethod = RequestMethod.valueOf(requestFirstLine[0]);
        String requestURL = requestFirstLine[1];

        HttpRequestHeader header = extractRequestHeader(bufferedReader);
        String body = IOUtils.readData(bufferedReader, header.getContentLength().orElse(0));

        return HttpRequest.of(requestMethod, requestURL, header, body);
    }

    private static String[] parseRequestFirstLine(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        if (line == null) {
            throw new InvalidRequestException();
        }

        String[] tokens = line.split(" ");
        if (tokens.length != 3) {
            throw new InvalidRequestException();
        }

        return tokens;
    }

    private static HttpRequestHeader extractRequestHeader(BufferedReader bufferedReader) throws IOException{
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!"".equals(line = bufferedReader.readLine())) {
            if (line == null) break;

            addLineToHeaders(line, headers);
        }
        return new HttpRequestHeader(headers);
    }

    private static void addLineToHeaders(String line, Map<String, String> headers) {
        int indexOfSeparator = line.indexOf(":");
        if (indexOfSeparator == -1) {
            throw new InvalidRequestException();
        }

        headers.put(line.substring(0, indexOfSeparator), line.substring(indexOfSeparator+1).trim());
    }

    public static Map<String, String> extractUserFromPath(String path) {
        String[] token = path.split("\\?");
        try {
            return extractUser(token[1]);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidQueryParameterException();
        }
    }

    public static Map<String, String> extractUser(String params) throws IndexOutOfBoundsException {
        String[] queryParams = params.split("&");
        return Arrays.stream(queryParams).map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0],  //key
                        a -> URLDecoder.decode(a[1])   //value
                ));
    }
}
