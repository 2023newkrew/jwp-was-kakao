package webserver.filter;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface Filter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain);
}
