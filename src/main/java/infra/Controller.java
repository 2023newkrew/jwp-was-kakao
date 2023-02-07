package infra;

import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;

public interface Controller {
    public HttpResponse response(HttpRequest request);
}
