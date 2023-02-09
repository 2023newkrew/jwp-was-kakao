package webserver.request;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static webserver.request.HttpRequestMethod.GET;
import static webserver.request.HttpRequestMethod.POST;

public class QueryStringParser {

    public static Map<String, String> parseQueryString(Query query, String separator) {
        String queryString = query.content;
        return Arrays.stream(queryString.split(separator))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(split -> split[0], split -> split[1]));
    }

    public static Query getQuery(HttpRequest request) {
        if (request.getMethod() == POST) {
            return Query.from(request.getBody());
        }
        if (request.getMethod() == GET) {
            return Query.from(request.getUri().getQuery());
        }
        throw new RuntimeException();
    }

    public static class Query {

        private final String content;

        private Query(String content) {
            this.content = content;
        }

        public static Query from(String content) {
            return new Query(content);
        }
    }
}
