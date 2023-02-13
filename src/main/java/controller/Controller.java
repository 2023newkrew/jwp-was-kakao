package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;

import java.io.IOException;
import java.util.UUID;

public abstract class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public ModelAndView process(HttpRequest request, HttpResponse response) throws IOException {
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

        return run(request, response);
    }

    protected abstract ModelAndView run(HttpRequest request, HttpResponse response);

}
