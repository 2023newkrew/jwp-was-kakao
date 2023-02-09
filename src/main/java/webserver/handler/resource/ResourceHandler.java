package webserver.handler.resource;

import org.springframework.http.HttpStatus;
import webserver.handler.Handler;
import webserver.handler.resource.resolver.Resolver;
import webserver.http.Content;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceHandler implements Handler {

    private final Resolver staticResolver;

    private final Resolver viewResolver;

    public ResourceHandler(Resolver staticResolver, Resolver viewResolver) {
        this.staticResolver = staticResolver;
        this.viewResolver = viewResolver;
    }

    @Override
    public boolean canHandle(Request request) {
        String path = request.getPath();

        return staticResolver.isResolvable(path) || viewResolver.isResolvable(path);
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();

        return new Response(HttpStatus.OK, getContent(path));
    }

    private Content getContent(String path) {
        if (staticResolver.isResolvable(path)) {
            return staticResolver.resolve(path);
        }
        return viewResolver.resolve(path);
    }
}
