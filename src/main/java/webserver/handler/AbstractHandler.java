package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

public abstract class AbstractHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public void service(final HttpRequest request, final HttpResponse response) {
        HttpMethod httpMethod = request.getMethod();
        if (HttpMethod.GET == httpMethod) {
            doGet(request, response);
        }
        else if (HttpMethod.POST == httpMethod) {
            doPost(request, response);
        }
        else {
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
            logger.error("사용할 수 없는 메소드입니다.");
        }
    }

    protected abstract void doGet(HttpRequest request, HttpResponse response);

    protected abstract void doPost(HttpRequest request, HttpResponse response);
}
