package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.MyHttpRequest;
import org.springframework.util.StringUtils;
import utils.IOUtils;

public class RequestParser {

    private static final String CONTENT_LENGTH = "Content-Length";
    private final BufferedReader br;

    private final Map<String, String> queryParams = new HashMap<>();
    private final Map<String, String> headerMap = new HashMap<>();
    private final Map<String, String> requestBody = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();

    public RequestParser(BufferedReader br) {
        this.br = br;
    }

    public MyHttpRequest buildHttpRequest() throws IOException {
        String line = br.readLine();
        String[] tokens = line.split(" ");
        String httpMethod = tokens[0];
        String requestUrl = tokens[1];

        // url parsing
        requestUrl = parseQueryParams(requestUrl, queryParams);

        // header parsing
        while (!StringUtils.isEmpty(line)) {
            parseHeaders(line);
            line = br.readLine();
        }

        parseBody(headerMap, requestBody);

        return new MyHttpRequest(httpMethod, requestUrl, queryParams, headerMap, cookies, requestBody);
    }

    private void parseHeaders(String line) {
        String[] headers = line.split(" ");
        if (headers[0].equals("Cookie:")) {
            parseCookie(line);
        }
        headerMap.put(headers[0].substring(0, headers[0].length() - 1), headers[1]);
    }

    private void parseCookie(String line) {
        String rawCookies = line.split(":")[1];
        for (String cookie : rawCookies.split(";")) {
            String key = cookie.split("=")[0];
            String value = cookie.split("=")[1];
            this.cookies.put(key, value);
        }
    }

    private void parseBody(Map<String, String> headerMap, Map<String, String> requestBody) throws IOException {
        if (!headerMap.containsKey(CONTENT_LENGTH)) {
            return;
        }

        String requestBodyString;
        requestBodyString = IOUtils.readData(br, Integer.parseInt(headerMap.get(CONTENT_LENGTH)));
        for (String query : requestBodyString.split("&")) {
            String[] keyValueSet = query.split("=");
            requestBody.put(keyValueSet[0], keyValueSet[1]);
        }
    }

    private String parseQueryParams(String requestUrl, Map<String, String> queryParams) {
        if (!requestUrl.contains("?")) {
            return requestUrl;
        }

        String queries = requestUrl.split("\\?")[1];
        requestUrl = requestUrl.split("\\?")[0];
        for (String query : queries.split("&")) {
            String[] keyValueSet = query.split("=");
            queryParams.put(keyValueSet[0], keyValueSet[1]);
        }

        return requestUrl;
    }
}
