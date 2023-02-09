package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * HTML 파일 요청을 처리하기 위한 컨트롤러
 */
public class ViewController extends Controller {

    private static final String ROOT_PATH = "templates/";
    private final String path;

    public ViewController(String path) {
        this.path = path;
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        super.doGet(request, response, dos);
        ResponseHeader responseHeader = response.getResponseHeader();
        RequestHeader requestHeader = request.getRequestHeader();

        String uri = requestHeader.get("URI").orElse("/index.html");

        // list로 접근했으나 쿠키에 로그인 정보가 없다면 로그인 화면으로 redirect
        if (uri.equals("/user/list.html")) {
            HttpCookie cookie = new HttpCookie(requestHeader.get("Cookie").orElse(""));
            boolean logined = Boolean.valueOf(cookie.get("logined").orElse("false"));
            if (!logined) {
                responseHeader.setHttpStatusCode(HttpStatusCode.REDIRECT);
                responseHeader.setLocation("/user/login.html");
                return;
            }
        }

        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(ROOT_PATH + path);

            if (body == null) {
                responseHeader.setHttpStatusCode(HttpStatusCode.NOT_FOUND);
                responseHeader.setContentType(ContentType.HTML);
                response.setResponseHeader(responseHeader);
                return;
            }
            responseHeader.setHttpStatusCode(HttpStatusCode.OK);
            responseHeader.setContentType(ContentType.HTML);
            responseHeader.setContentLength(body.length);

            response.setResponseHeader(responseHeader);
            response.setResponseBody(body);
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
