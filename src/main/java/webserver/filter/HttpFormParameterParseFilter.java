package webserver.filter;

import http.*;
import webserver.http.HttpRequestParamParser;

import java.util.List;

public class HttpFormParameterParseFilter implements Filter {
    private static final HttpRequestParamParser parser = new HttpRequestParamParser();

    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse, FilterChain filterChain) {
        if (hasFormParameter(httpRequest)) {
            HttpRequestParams params = parser.parse(httpRequest.getBody());
            httpRequest.getParameters().setParameters(params.getParameters());

            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    private boolean hasFormParameter(HttpRequest httpRequest) {
        List<String> contentType = httpRequest.getHeaders().getHeader(HttpHeaders.CONTENT_TYPE);
        return contentType != null && contentType.contains(HttpContentType.APPLICATION_X_WWW_FORM_URLENCODE.getValue());
    }
}
