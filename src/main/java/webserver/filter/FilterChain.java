package webserver.filter;

import http.HttpRequest;
import http.HttpResponse;
import webserver.handler.Handler;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FilterChain {
    private final List<Map.Entry<List<String>, Filter>> filters;
    private final Handler handler;

    private int filterIdx = -1;

    public FilterChain(List<Map.Entry<List<String>, Filter>> filters, Handler handler) {
        this.filters = filters;
        this.handler = handler;
    }

    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse) {
        while (++filterIdx < filters.size() && !findMatchedFilter(httpRequest));

        if (isFilteringFinished()) {
            handler.handle(httpRequest, httpResponse);
            return;
        }

        Filter filter = filters.get(filterIdx).getValue();
        filter.doFilter(httpRequest, httpResponse, this);
    }

    private boolean isFilteringFinished() {
        return filterIdx >= filters.size();
    }

    private boolean findMatchedFilter(HttpRequest httpRequest) {
        List<String> urlMappingRegex = filters.get(filterIdx).getKey();
        return urlMappingRegex.stream()
                .anyMatch(regex -> Pattern.matches(regex, httpRequest.getURL()));
    }
}
