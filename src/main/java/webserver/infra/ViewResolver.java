package webserver.infra;

import lombok.experimental.UtilityClass;
import model.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static constant.DefaultConstant.DEFAULT_BODY;
import static constant.DefaultConstant.DEFAULT_PATH;
import static constant.PathConstant.STATIC;
import static constant.PathConstant.TEMPLATES;
import static utils.FileIoUtils.*;
import static utils.ResponseUtils.*;

@UtilityClass
public class ViewResolver {
    public void resolve(HttpRequest request, DataOutputStream dos) {
        byte[] body = DEFAULT_BODY;

        if (!request.getUrl().equals(DEFAULT_PATH)) {
            String requestURL = request.getUrl();
            body = getBody(request, requestURL);
        }

        response200Header(dos, request, body.length);
        responseBody(dos, body);
    }

    private byte[] getBody(HttpRequest httpRequest, String requestURL) {
        try {
            if (isStaticPath(requestURL)) {
                return loadFileFromClasspath(STATIC + httpRequest.getUrl());
            }
            return loadFileFromClasspath(TEMPLATES + httpRequest.getUrl());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }
}
