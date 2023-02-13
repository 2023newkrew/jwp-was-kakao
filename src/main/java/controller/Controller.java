package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;

import java.io.IOException;
import java.util.UUID;

public abstract class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public void process(HttpRequest request, HttpResponse response) throws IOException {
        RequestHeader requestHeader = request.getRequestHeader();

        // 요청 헤더에 쿠키가 없다면 생성하여 응답 전송
        // TODO: Optional 활용 개성방안?
        HttpCookie requestCookie = new HttpCookie(requestHeader.get("Cookie").orElse(""));

        // 최초 접속 상황
        if (requestCookie.get("JSESSIONID").isEmpty()) {
            ResponseHeader header = new ResponseHeader();
            HttpCookie httpCookie = new HttpCookie();
            httpCookie.put("JSESSIONID", UUID.randomUUID().toString());
            httpCookie.put("Path", "/");

            header.setHttpCookie(httpCookie);
            response.setResponseHeader(header);
        }

        String method = requestHeader.get("method").orElseThrow(IllegalArgumentException::new);
        if (method.equals("GET")) {
            doGet(request, response);
        }
        if (method.equals("POST")) {
            doPost(request, response);
        }
        doFinally(request, response);

        response.send();
    }

    protected void doGet(HttpRequest request, HttpResponse response) {}
    protected void doPost(HttpRequest request, HttpResponse response) {}
    protected void doFinally(HttpRequest request, HttpResponse response) {}
}
