package model.request.properties;

import lombok.Getter;
import model.enumeration.HttpMethod;

import java.util.HashMap;

import static utils.parser.QueryStringParser.*;

@Getter
public class HttpRequestFirstLineProperties {
    private final String QUERY_STRING_STARTING_POINT_REGEX = "\\?";
    private final String QUERY_STRING_STARTING_POINT = "?";

    private HttpMethod httpMethod;
    private String URL;
    private String httpProtocol;
    private QueryParams queryParams;
    public HttpRequestFirstLineProperties(String line) {
        this.httpMethod = HttpMethod.valueOf(line.split(" ")[0]);
        this.URL = line.split(" ")[1];
        this.httpProtocol = line.split(" ")[2];

        setQueryParamsMapIfExists();
    }

    private void setQueryParamsMapIfExists() {
        if (isURLHasQueryString()) {
            URL = URL.split(QUERY_STRING_STARTING_POINT_REGEX)[0];
            queryParams = QueryParams.of(parseQueryString(URL.split(QUERY_STRING_STARTING_POINT_REGEX)[1]));
            return;
        }
        queryParams = QueryParams.of(new HashMap<>());
    }

    private boolean isURLHasQueryString() {
        return URL.contains(QUERY_STRING_STARTING_POINT);
    }
}
