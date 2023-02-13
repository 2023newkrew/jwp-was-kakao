package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 관련 URI: -
 * 미리 정의된 URI들을 제외한 모든 요청들(static 파일 등)을 처리하는 컨트롤러
 */
public class MainController implements Controller {
    private final SessionManager sessionManager;

    public MainController(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ModelAndView run(HttpRequest request, HttpResponse response) {

        HttpSession session = sessionManager.getSession(request, response);

        String root = "static";
        String uri = request.getRequestHeader().get("URI").orElseThrow(IllegalArgumentException::new);
        String[] split = uri.split("\\.");
        String fileType = split[split.length - 1];

        if (fileType.equals("html")) {
            root = "templates";
        }

        // 로그인 상태로 /user/login.html 접근시 redirect
        Object logined = session.getAttribute("logined");
        if (uri.equals("/user/login.html")) {
            if (logined != null && logined.equals(true)) {
                return new ModelAndView("redirect:/index.html");
            }
        }

        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(root + uri);
            ResponseHeader responseHeader = response.getResponseHeader();

            if (body == null) {
                responseHeader.setHttpStatusCode(HttpStatusCode.NOT_FOUND);
                responseHeader.setContentType(ContentType.HTML);
            }

            responseHeader.setContentType(ContentType.valueOf(fileType.toUpperCase()));
            responseHeader.setContentLength(body.length);

            response.setResponseHeader(responseHeader);
            response.setResponseBody(body);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
