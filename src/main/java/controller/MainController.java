package controller;

import type.ContentType;
import type.HttpStatusCode;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.RequestHeader;
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
    public void process(HttpRequest request, DataOutputStream dos) throws IOException {
        RequestHeader header = request.getRequestHeader();
        String reqMethod = header.get("method").orElseThrow(IllegalArgumentException::new);
        String uri = header.get("URI").orElseThrow(IllegalArgumentException::new);

        if (reqMethod.equals("GET")) {
            String root = "static";
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
            } catch (URISyntaxException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
