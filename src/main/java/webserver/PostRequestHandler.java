package webserver;

public abstract class PostRequestHandler implements HttpRequestHandler {

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getTarget()
                .getMethod() == HttpMethod.POST;
    }

    @Override
    public abstract HttpResponse handle(HttpRequest request);
}
