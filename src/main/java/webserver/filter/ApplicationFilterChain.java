package webserver.filter;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.List;

public class ApplicationFilterChain implements FilterChain {

    private final List<Filter> filters;
    private int nextFilterIdx = 0;

    public ApplicationFilterChain(final FilterConfig filterConfig) {
        this.filters = filterConfig.getFilters();
    }

    @Override
    public void doFilter(final HttpRequest request, final HttpResponse response) {
        if (nextFilterIdx < filters.size()) {
            filters.get(nextFilterIdx++).doFilter(request, response, this);
        }
    }
}
