package webserver.filter;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.util.List;

public class MyFilterChain {

    private final List<MyFilter> filters = List.of(
            SessionFilter.getInstance()
    );

    private int currentFilterIndex = 0;

    public void doFilter(HttpRequest request, HttpResponse.Builder responseBuilder) {
        currentFilterIndex++;
        if (currentFilterIndex > filters.size()) {
            return;
        }
        filters.get(currentFilterIndex - 1)
                .doFilter(request, responseBuilder, this);
    }
}
