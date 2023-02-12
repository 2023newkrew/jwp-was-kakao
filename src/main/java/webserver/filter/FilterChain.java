package webserver.filter;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface FilterChain {

    void doFilter(HttpRequest request, HttpResponse response);
}
