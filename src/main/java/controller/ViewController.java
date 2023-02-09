package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.HttpRequest;
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
    protected void doGet(HttpRequest request, DataOutputStream dos) {
        super.doGet(request, dos);
        try {
            byte[] returnBody = FileIoUtils.loadFileFromClasspath(ROOT_PATH + path);

            if (returnBody == null) {
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.NOT_FOUND, ContentType.HTML).getValue());
                dos.flush();
                return;
            }

            dos.writeBytes(
                    ResponseHeader.of(HttpStatusCode.OK,
                                    ContentType.HTML,
                                    returnBody.length)
                            .getValue()
            );

            dos.write(returnBody);
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
