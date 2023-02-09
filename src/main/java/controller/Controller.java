package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpCookie;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.RequestHeader;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Controller {

    static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public void process(HttpRequest request, HttpResponse response, DataOutputStream dos) throws IOException {
        RequestHeader requestHeader = request.getRequestHeader();
        String method = requestHeader.get("method").orElseThrow(IllegalArgumentException::new);
        if (method.equals("GET")) {
            doGet(request, response, dos);
        }
        if (method.equals("POST")) {
            doPost(request, response, dos);
        }
        doFinally(request, response, dos);

        // 요청 헤더에 쿠키가 없다면 생성하여 응답 전송
        // TODO: Optional 활용 개성방안?
        HttpCookie requestCookie = new HttpCookie(requestHeader.get("Cookie").orElse(""));

        if (requestCookie.get("JSESSIONID").isEmpty()) {
            response.getResponseHeader().setCookie(new HttpCookie());
        }
        sendResponse(response, dos);
    }

    protected void doGet(HttpRequest request, HttpResponse response, DataOutputStream dos) {}
    protected void doPost(HttpRequest request, HttpResponse response, DataOutputStream dos) {}
    protected void doFinally(HttpRequest request, HttpResponse response, DataOutputStream dos) {}

    private static void sendResponse(HttpResponse response, DataOutputStream dos) throws IOException {
        if (response.getResponseHeader() != null) {
            dos.writeBytes(response.getResponseHeader().getValue());
        }
        if (response.getResponseBody() != null) {
            dos.write(response.getResponseBody());
        }
        dos.flush();
    }
}
