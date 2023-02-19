package filter;

import http.request.Request;

public interface Filter {
    void doFilter(Request request);
}
