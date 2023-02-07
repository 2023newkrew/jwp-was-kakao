package webserver.infra;

import constant.HeaderConstant;
import lombok.experimental.UtilityClass;
import model.TemplateLoadResult;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.response.ResponseHeader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import static constant.DefaultConstant.*;
import static constant.HeaderConstant.*;
import static constant.PathConstant.STATIC;
import static constant.PathConstant.TEMPLATES;
import static utils.FileIoUtils.getStaticFolderNames;
import static utils.FileIoUtils.loadFileFromClasspath;
import static utils.ResponseUtils.*;

@UtilityClass
public class ViewResolver {
    public void resolve(HttpRequest request, HttpResponse response, DataOutputStream dos) {
        byte[] body = DEFAULT_BODY;

        if (request.isNotDefaultURL()) {
            body = getBody(request, request.getURL(), dos);
        }

        response.setAttribute(CONTENT_TYPE, request.findHeaderValue(ACCEPT, DEFAULT_CONTENT_TYPE).split(",")[0]);
        response.setAttribute(CONTENT_LENGTH, String.valueOf(body.length));
        response200Header(dos, response);
        responseBody(dos, body);
    }

    public void resolve(TemplateLoadResult templateLoadResult, HttpResponse response, DataOutputStream dos) {
        System.out.println("templateLoadResult.getContent() = " + templateLoadResult.getContent());
        byte[] body = templateLoadResult.getContent().getBytes();

        response.setAttribute(CONTENT_TYPE, DEFAULT_CONTENT_TYPE.split(",")[0]);
        response.setAttribute(CONTENT_LENGTH, String.valueOf(body.length));
        response200Header(dos, response);
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
