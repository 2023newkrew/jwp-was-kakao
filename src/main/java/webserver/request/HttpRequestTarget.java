package webserver.request;

import lombok.Getter;
import webserver.constant.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;

@Getter
public class HttpRequestTarget {

    private static final String QUERY_START_DELIMITER = "?";

    public static final String FIRST_LINE_SPLIT_DELIMITER = " ";

    private HttpMethod method;

    private String path;

    private QueryParams queryParams;

    public HttpRequestTarget(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        String[] elements = firstLine.split(FIRST_LINE_SPLIT_DELIMITER);
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
        this.queryParams = new QueryParams(targetUrl.substring(queryStartIndex + 1));
    }


}
