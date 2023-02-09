package framework.request;

import framework.utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private RequestParser() {
    }

    public static Request getRequestFrom(BufferedReader bufferedReader) throws IOException {
        RequestHeader requestHeader = parseHeader(bufferedReader);
        Map<String, String> requestParams = parseParams(getParamsFromRequest(bufferedReader, requestHeader));
        HttpCookie cookie = HttpCookie.from(requestHeader.get("Cookie").orElse(""));

        return new Request(requestHeader, requestParams, cookie);
    }

    private static RequestHeader parseHeader(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            if (line == null) {
                throw new IOException();
            }
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        return new RequestHeader(stringBuilder.toString());
    }

    private static String getParamsFromRequest(BufferedReader bufferedReader, RequestHeader requestHeader) throws IOException {
        int contentLength = Integer.parseInt(requestHeader.get("Content-Length").orElse("0"));
        String requestBody = IOUtils.readData(bufferedReader, contentLength);
        if (requestHeader.getMethod().equals("GET")) {
            requestBody = requestHeader.parseGetParams();
        }
        return requestBody;
    }

    private static Map<String, String> parseParams(String requestBody) {
        if (requestBody == null || !requestBody.contains("=")) {
            return null;
        }
        return extractParams(requestBody);
    }

    private static Map<String, String> extractParams(String requestParams) {
        Map<String, String> params = new HashMap<>();
        String[] sets = requestParams.split("&");
        for (String set : sets) {
            String[] split = set.split("=");
            params.put(split[0], split[1]);
        }
        return params;
    }
}
