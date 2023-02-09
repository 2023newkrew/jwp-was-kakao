package logics.controller.get;

import logics.controller.Controller;
import utils.requests.HttpRequest;
import utils.response.HttpResponse;
import utils.response.HttpResponseVersion1;

import java.nio.charset.StandardCharsets;

/**
 * Default Web Page when other controllers cannot apprehend url.
 */
public class DefaultResponseController implements Controller {
    @Override
    public HttpResponse makeResponse(HttpRequest httpRequest) {
        return new HttpResponseVersion1()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/html;charset=utf-8")
                .setBody("Hello world".getBytes(StandardCharsets.UTF_8));
    }
}
