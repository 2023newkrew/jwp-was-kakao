package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 관련 URI: -
 * 미리 정의된 URI들을 제외한 모든 요청들(static 파일 등)을 처리하는 컨트롤러
 */
public class MainController extends Controller {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String root = "static";
        String uri = request.getRequestHeader().get("URI").orElseThrow(IllegalArgumentException::new);
        String[] split = uri.split("\\.");
        String fileType = split[split.length - 1];

        if (fileType.equals("html")) {
            root = "templates";
        }

        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(root + uri);
            ResponseHeader responseHeader = response.getResponseHeader();

            if (body == null) {
                responseHeader.setHttpStatusCode(HttpStatusCode.NOT_FOUND);
                responseHeader.setContentType(ContentType.HTML);
            }

            responseHeader.setHttpStatusCode(HttpStatusCode.OK);
            responseHeader.setContentType(ContentType.valueOf(fileType.toUpperCase()));
            responseHeader.setContentLength(body.length);

            response.setResponseHeader(responseHeader);
            response.setResponseBody(body);
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
