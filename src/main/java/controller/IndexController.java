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
 * 관련 URI: /index.html, /
 */
public class IndexController extends Controller {

    private static final String INDEX_FILE_PATH = "templates/index.html";

    @Override
    public void process(HttpRequest request, DataOutputStream dos) throws IOException {
        String reqMethod = request.getRequestHeader().get("method").orElseThrow(IllegalArgumentException::new);

        if (reqMethod.equals("GET")) {
            try {
                byte[] returnBody = FileIoUtils.loadFileFromClasspath(INDEX_FILE_PATH);

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

                responseBody(dos, returnBody);
            } catch (URISyntaxException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
