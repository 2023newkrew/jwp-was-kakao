package webserver.handler;

import org.springframework.http.HttpStatus;
import webserver.handler.resolver.Resolvers;
import webserver.http.content.Content;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.ResponseHeader;

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

        return new Response(new ResponseHeader(HttpStatus.OK, content), content);
    }
}
