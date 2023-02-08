package logics.get;

import logics.Controller;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.nio.charset.StandardCharsets;

public class DefaultResponseController extends Controller {
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        return new HttpResponseVersion1()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/html;charset=utf-8")
                .setBody("Hello world".getBytes(StandardCharsets.UTF_8));
    }
}
