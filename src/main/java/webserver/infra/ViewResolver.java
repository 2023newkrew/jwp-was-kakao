package webserver.infra;

import lombok.experimental.UtilityClass;
import model.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static constant.DefaultConstant.DEFAULT_BODY;
import static constant.DefaultConstant.DEFAULT_URL;
import static constant.PathConstant.STATIC;
import static constant.PathConstant.TEMPLATES;
import static utils.FileIoUtils.*;
import static utils.ResponseUtils.*;

@UtilityClass
public class ViewResolver {
    public void resolve(HttpRequest request, DataOutputStream dos) {
        byte[] body = DEFAULT_BODY;

        if (request.isNotDefaultURL()) {
            body = getBody(request, request.getURL(), dos);
        }

        response200Header(dos, request, body.length);
        responseBody(dos, body);
    }

    private byte[] getBody(HttpRequest httpRequest, String requestURL, DataOutputStream dos) {
        try {
            if (isStaticPath(requestURL)) {
                return loadFileFromClasspath(STATIC + httpRequest.getURL());
            }
            return loadFileFromClasspath(TEMPLATES + httpRequest.getURL());
        } catch (IOException | URISyntaxException | NullPointerException e) {
            response404Header(dos);
            throw new RuntimeException(e);
        }
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }
}
