package webserver.handler.resource;

import org.springframework.http.HttpStatus;
import webserver.handler.Handler;
import webserver.handler.resource.resolver.Resolver;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBody;
import webserver.response.ResponseHeader;

public class ResourceHandler implements Handler {

    private final Resolver staticResolver;

    private final Resolver viewResolver;

    public ResourceHandler(Resolver staticResolver, Resolver viewResolver) {
        this.staticResolver = staticResolver;
        this.viewResolver = viewResolver;
    }

    @Override
    public boolean canHandle(Request request) {
        return isResolvable(request.getPath());
    }

    private boolean isResolvable(String path) {
        return staticResolver.isResolvable(path) || viewResolver.isResolvable(path);
    }

    @Override
    public Response handle(Request request) {
        ResponseBody responseBody = resolve(request.getPath());
        ResponseHeader header = new ResponseHeader(HttpStatus.OK, responseBody);

        return new Response(header, responseBody);
    }

    private ResponseBody resolve(String path) {
        if (staticResolver.isResolvable(path)) {
            return staticResolver.resolve(path);
        }
        return viewResolver.resolve(path);
    }
}
