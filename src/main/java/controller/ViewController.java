package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ResponseHeader;

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
