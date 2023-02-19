package webserver.filter;

import http.HttpRequest;
import http.HttpResponse;

import java.util.List;

public interface Filter {
    void doFilter(HttpRequest httpRequest, HttpResponse httpResponse, FilterChain filterChain);
}
