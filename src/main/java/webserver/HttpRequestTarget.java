package webserver;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequestTarget {

    private static final String QUERY_START_DELIMITER = "?";

    private static final String QUERY_SPLIT_DELIMITER = "&";

    private static final String PARAM_SPLIT_DELIMITER = "=";

    private HttpMethod method;

    private String path;

    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequestTarget(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        String[] elements = firstLine.split(" ");
        this.method = HttpMethod.valueOf(elements[0]);
        parseTargetUrl(elements[1]);
    }

    private void parseTargetUrl(String targetUrl) {
        int queryStartIndex = targetUrl.indexOf(QUERY_START_DELIMITER);
        if (queryStartIndex == -1) {
            this.path = targetUrl;
            return;
        }
        this.path = targetUrl.substring(0, queryStartIndex);
        parseQueryParams(targetUrl.substring(queryStartIndex + 1));
    }

    private void parseQueryParams(String query) {
        String[] elements = query.split(QUERY_SPLIT_DELIMITER);
        for (String param : elements) {
            String[] kv = param.split(PARAM_SPLIT_DELIMITER);
            queryParams.put(kv[0], kv[1]);
        }
    }
}
