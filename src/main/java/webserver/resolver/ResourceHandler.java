package webserver.resolver;

import org.springframework.http.HttpStatus;
import webserver.content.Content;
import webserver.handler.Handler;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceHandler implements Handler {

    private final Resolvers resolvers;

    public ResourceHandler(Resolvers resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public boolean canHandle(Request request) {
        return resolvers.isResolvable(request.getPath());
    }

    @Override
    public Response handle(Request request) {
        Content content = resolvers.resolve(request.getPath());

        return new Response(HttpStatus.OK, content);
    }
}
