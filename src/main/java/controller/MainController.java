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
 * 관련 URI: -
 * 미리 정의된 URI들을 제외한 모든 요청들(static 파일 등)을 처리하는 컨트롤러
 */
public class MainController extends Controller {
    @Override
    public void doGet(HttpRequest request, DataOutputStream dos) {
        String root = "static";
        String uri = request.getRequestHeader().get("URI").orElseThrow(IllegalArgumentException::new);
        String[] split = uri.split("\\.");
        String fileType = split[split.length - 1];

        if (fileType.equals("html")) {
            root = "templates";
        }

        try {
            byte[] returnBody = FileIoUtils.loadFileFromClasspath(root + uri);

            if (returnBody == null) {
                dos.writeBytes(ResponseHeader.of(HttpStatusCode.NOT_FOUND, ContentType.HTML).getValue());
                dos.flush();
                return;
            }

            dos.writeBytes(
                    ResponseHeader.of(HttpStatusCode.OK,
                                    ContentType.valueOf(fileType.toUpperCase()),
                                    returnBody.length)
                            .getValue()
            );

            responseBody(dos, returnBody);
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpRequest request, DataOutputStream dos) {

    }

    @Override
    public void doFinally(HttpRequest request, DataOutputStream dos) {

    }
}
