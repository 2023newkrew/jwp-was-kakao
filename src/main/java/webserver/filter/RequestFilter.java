package webserver.filter;

import webserver.http.request.HttpRequest;

public interface RequestFilter {
    void doFilter(HttpRequest request);
}
