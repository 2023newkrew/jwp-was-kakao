package webserver;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface MyFilter {

    void doFilter(HttpRequest request, HttpResponse.Builder responseBuilder, MyFilterChain filterChain);
}
