package webserver.infra;

import lombok.experimental.UtilityClass;
import model.dto.view.TemplateLoadResult;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.response.properties.ResponseBody;
import model.response.properties.ResponseHeader;
import utils.builder.ResponseBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

import static constant.DefaultConstant.*;
import static constant.HeaderConstant.*;
import static constant.PathConstant.*;
import static utils.utils.FileIoUtils.*;

@UtilityClass
public class ViewResolver {
    private final String CONTENT_TYPE_DELIMITER = ",";
    private final String REQUEST_PATH_DELIMITER = "/";

    public HttpResponse resolve(HttpRequest request) {
        ResponseBody responseBody = getBody(request.getURL());

        ResponseHeader header = new ResponseHeader();
        header.setAttribute(CONTENT_TYPE, getMostPreferredAcceptContentType(request));
        header.setAttribute(CONTENT_LENGTH, String.valueOf(responseBody.length()));

        return ResponseBuilder.ok()
                .header(header)
                .body(responseBody)
                .build();
    }


    public HttpResponse resolve(TemplateLoadResult templateLoadResult) {
        ResponseBody responseBody = new ResponseBody(templateLoadResult.getContent().getBytes());

        ResponseHeader header = new ResponseHeader();
        header.setAttribute(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        header.setAttribute(CONTENT_LENGTH, String.valueOf(responseBody.length()));

        return ResponseBuilder.ok()
                .header(header)
                .body(responseBody)
                .build();
    }

    private ResponseBody getBody(String requestURL) {
        try {
            if (requestURL.equals(DEFAULT_URL)) {
                return new ResponseBody(DEFAULT_BODY);
            }

            if (isStaticPath(requestURL)) {
                return new ResponseBody(loadFileFromClasspath(STATIC + requestURL));
            }

            return new ResponseBody(loadFileFromClasspath(TEMPLATES + requestURL));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(getPath(requestURL));
    }

    private String getPath(String requestURL) {
        return requestURL.split(REQUEST_PATH_DELIMITER)[1];
    }

    private String getMostPreferredAcceptContentType(HttpRequest request) {
        return request.findHeaderValue(ACCEPT, DEFAULT_CONTENT_TYPE)
                .split(CONTENT_TYPE_DELIMITER)[0];
    }
}
