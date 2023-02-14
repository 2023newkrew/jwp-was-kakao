package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

import java.util.List;

public class Handlers {
    private final List<Handler> handlers;

    public Handlers(Handler... handlers) {
        this(List.of(handlers));
    }

    public Handlers(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public Response handle(Request request) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(request))
                .findAny()
                .orElseThrow(RuntimeException::new)
                .handle(request);
    }
}
