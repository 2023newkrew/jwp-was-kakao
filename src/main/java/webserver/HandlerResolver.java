package webserver;

import java.util.List;

public class HandlerResolver {

    private final List<HttpRequestHandler> handlers;

    private HandlerResolver() {
        handlers = List.of(
                TemplateResourceRequestHandler.getInstance(),
                StaticResourceRequestHandler.getInstance(),
                UserCreateRequestHandler.getInstance()
        );
    }

    private static class HandlerResolverHolder {
        public static final HandlerResolver INSTANCE = new HandlerResolver();
    }

    public static HandlerResolver getInstance() {
        return HandlerResolverHolder.INSTANCE;
    }

    public HttpRequestHandler resolve(HttpRequest request) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(request))
                .findFirst()
                .orElse(NotFoundHandler.getInstance());
    }
}
