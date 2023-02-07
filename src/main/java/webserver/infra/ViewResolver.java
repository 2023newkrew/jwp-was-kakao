package webserver.infra;

import lombok.experimental.UtilityClass;
import model.TemplateLoadResult;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.response.ResponseBody;
import model.response.ResponseHeader;
import utils.ResponseBuilder;
import utils.ResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static constant.DefaultConstant.*;
import static constant.HeaderConstant.*;
import static constant.PathConstant.*;
import static utils.FileIoUtils.*;
import static utils.ResponseUtils.*;

@UtilityClass
public class ViewResolver {
    public static final String CONTENT_TYPE_DELIMITER = ",";
    public HttpResponse resolve(HttpRequest request) {
        ResponseBody body = getBody(request.getURL());

        ResponseHeader header = new ResponseHeader();
        header.put(CONTENT_TYPE, request.findHeaderValue(ACCEPT, DEFAULT_CONTENT_TYPE).split(",")[0]);
        header.put(CONTENT_LENGTH, String.valueOf(body.length()));

        return ResponseBuilder.ok()
                .header(header)
                .body(getBody(request.getURL()))
                .build();
    }

    public HttpResponse resolve(TemplateLoadResult templateLoadResult) {
        ResponseBody responseBody = new ResponseBody(templateLoadResult.getContent().getBytes());

        ResponseHeader header = new ResponseHeader();
        header.put(CONTENT_TYPE, DEFAULT_CONTENT_TYPE.split(CONTENT_TYPE_DELIMITER)[0]);
        header.put(CONTENT_LENGTH, String.valueOf(responseBody.length()));

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
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }
}
