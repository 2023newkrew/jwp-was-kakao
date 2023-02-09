package webserver.filter;

import http.HttpRequest;
import http.HttpResponse;
import webserver.handler.Handler;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Filters {
    private final List<Map.Entry<List<String>, Filter>> filters = new ArrayList<>();

    public void addFilter(List<String> urlMappingRegex, Filter filter) {
        filters.add(new AbstractMap.SimpleEntry<>(urlMappingRegex, filter));
    }

    public void addFilter(List<String> urlMappingRegex, Filter filter, int order) {
        if (order < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (order > filters.size()) {
            filters.add(new AbstractMap.SimpleEntry<>(urlMappingRegex, filter));
            return;
        }
        filters.add(order, new AbstractMap.SimpleEntry<>(urlMappingRegex, filter));
    }

    public void doFilterAndHandle(HttpRequest httpRequest, HttpResponse httpResponse, Handler handler) {
        FilterChain filterChain = new FilterChain(filters, handler);
        filterChain.doFilter(httpRequest, httpResponse);
    }
}
