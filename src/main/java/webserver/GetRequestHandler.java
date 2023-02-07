package webserver;

public abstract class GetRequestHandler implements HttpRequestHandler {

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getTarget()
                .getMethod() == HttpMethod.GET;
    }

    @Override
    public abstract HttpResponse handle(HttpRequest request);
}
