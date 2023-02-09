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
    public void doGet(HttpRequest request, DataOutputStream dos) {
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
