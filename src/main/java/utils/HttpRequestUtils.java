package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import webserver.request.HttpMethod;
import webserver.session.HttpSession;

public class HttpRequestUtils {
    private static final String COOKIE_SPLIT_DELIMITER = ";";
    private static final String COOKIE_ATTRIBUTE_SPLIT_DELIMITER = "=";
    private static final String REQUEST_LINE_SPLIT_DELIMITER = " ";
    private static final Integer METHOD_INDEX_IN_REQUEST_LINE = 0;
    private static final Integer PATH_INDEX_IN_REQUEST_LINE = 1;

    public static Map<String, String> parseCookie(String cookie) {
        Map<String, String> results = new HashMap<>();
        if (cookie == null) {
            return results;
        }
        String[] parsedCookie = cookie.split(COOKIE_SPLIT_DELIMITER);
        for (String cookieHeader : parsedCookie) {
            String[] splitAttributeString = cookieHeader.split(COOKIE_ATTRIBUTE_SPLIT_DELIMITER);
            results.put(splitAttributeString[0].trim(), splitAttributeString[1].trim());
        }
        return results;
    }

    public static String convertSessionIdToString(String sessionId) {
        return HttpSession.SESSION_ID_NAME + "=" + sessionId;
    }

    public static HttpMethod getHttpMethodFromRequestLine(String httpRequestLine) {
        return HttpMethod.valueOf(httpRequestLine.split(REQUEST_LINE_SPLIT_DELIMITER)[METHOD_INDEX_IN_REQUEST_LINE]);
    }

    public static String getPathFromRequestLine(String httpRequestLine) {
        return httpRequestLine.split(REQUEST_LINE_SPLIT_DELIMITER)[PATH_INDEX_IN_REQUEST_LINE];
    }

    public static String parseHttpBody(BufferedReader bufferedReader, Map<String, String> headers) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return "";
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        return IOUtils.readData(bufferedReader, contentLength);
    }

    public static Map<String, String> parseHttpHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            int index = line.indexOf(":");
            header.put(line.substring(0, index), line.substring(index + 1)
                    .trim());
            line = bufferedReader.readLine();
        }

        return header;
    }
}
