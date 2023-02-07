package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.HttpRequest;
import org.springframework.util.StringUtils;
import utils.IOUtils;

public class RequestParser {

    private static final String CONTENT_LENGTH = "Content-Length";
    private final BufferedReader br;

    public RequestParser(BufferedReader br) {
        this.br = br;
    }

    public HttpRequest buildHttpRequest() throws IOException {
        String line = br.readLine();
        String[] tokens = line.split(" ");
        String httpMethod = tokens[0];
        String requestUrl = tokens[1];
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> requestBody = new HashMap<>();

        // url parsing
        requestUrl = parseQueryParams(requestUrl, queryParams);

        // header parsing
        while (!StringUtils.isEmpty(line)) {
            parseHeaders(line, headerMap);
            line = br.readLine();
        }

        parseBody(headerMap, requestBody);

        return new HttpRequest(httpMethod, requestUrl, queryParams, headerMap, requestBody);
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

    private void parseHeaders(String line, Map<String, String> headerMap) {
        String[] headers = line.split(" ");
        headerMap.put(headers[0].substring(0, headers[0].length() - 1), headers[1]);
    }
}
