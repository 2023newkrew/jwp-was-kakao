package webserver.handler.resolver;

import webserver.handler.HttpRequestHandler;
import webserver.handler.NotFoundHandler;
import webserver.handler.gethandler.impl.StaticResourceRequestHandler;
import webserver.handler.gethandler.impl.TemplateResourceRequestHandler;
import webserver.handler.gethandler.impl.UserListRequestHandler;
import webserver.handler.posthandler.impl.LoginRequestHandler;
import webserver.handler.posthandler.impl.UserCreateRequestHandler;
import webserver.request.HttpRequest;

import java.util.List;

public class HandlerResolver {

    private final List<HttpRequestHandler> handlers;

    private HandlerResolver() {
        handlers = List.of(
                TemplateResourceRequestHandler.getInstance(),
                StaticResourceRequestHandler.getInstance(),
                UserCreateRequestHandler.getInstance(),
                LoginRequestHandler.getInstance(),
                UserListRequestHandler.getInstance()
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
