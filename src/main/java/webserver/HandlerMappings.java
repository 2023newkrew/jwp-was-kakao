package webserver;

import http.HttpRequest;
import webserver.handler.Handler;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UrlMappingHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HandlerMappings {
    private final Map<String, UrlMappingHandler> urlMappingHandlerMapping = new HashMap<>();
    private final Handler defaultHandler;

    public HandlerMappings() {
        this.defaultHandler = new StaticResourceRequestHandler();
    }

    public HandlerMappings(Handler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void addUrlMappingHandler(UrlMappingHandler handler) {
        urlMappingHandlerMapping.put(handler.getUrlMappingRegex(), handler);
    }

    public Handler findHandler(HttpRequest httpRequest) {
        Handler handler = getUrlHandler(httpRequest);
        if (handler != null) {
            return handler;
        }
        return defaultHandler;
    }

    private Handler getUrlHandler(HttpRequest httpRequest) {
        return urlMappingHandlerMapping.keySet().stream()
                .filter(urlMapping -> Pattern.compile(urlMapping).matcher(httpRequest.getURL()).matches())
                .map(urlMappingHandlerMapping::get)
                .filter(handler -> handler.support(httpRequest))
                .findFirst()
                .orElse(null);
    }
}
