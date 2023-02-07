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
    public void resolve(HttpRequest request, DataOutputStream dos) throws IOException {
        ResponseBody body = getBody(request.getURL());

        ResponseHeader header = new ResponseHeader();
        header.put(CONTENT_TYPE, request.findHeaderValue(ACCEPT, DEFAULT_CONTENT_TYPE).split(",")[0]);
        header.put(CONTENT_LENGTH, String.valueOf(body.length()));

        HttpResponse response = ResponseBuilder.ok()
                .header(header)
                .body(getBody(request.getURL()))
                .build();

        doResponse(dos, response);
    }

    public void resolve(TemplateLoadResult templateLoadResult, DataOutputStream dos) throws IOException {
        ResponseBody responseBody = new ResponseBody(templateLoadResult.getContent().getBytes());

        ResponseHeader header = new ResponseHeader();
        header.put(CONTENT_TYPE, DEFAULT_CONTENT_TYPE.split(",")[0]);
        header.put(CONTENT_LENGTH, String.valueOf(responseBody.length()));

        HttpResponse response = ResponseBuilder.ok()
                .header(header)
                .body(responseBody)
                .build();

        doResponse(dos, response);
    }

    private ResponseBody getBody(String requestURL) {
        // todo: 여기 어떻게 못하나 ㅋㅋ
        try {
            if (requestURL.equals(DEFAULT_URL)) {
                return new ResponseBody(DEFAULT_BODY);
            }

            if (isStaticPath(requestURL)) {
                return new ResponseBody(loadFileFromClasspath(STATIC + requestURL));
            }

            return new ResponseBody(loadFileFromClasspath(TEMPLATES + requestURL));
        } catch (IOException | URISyntaxException | NullPointerException e) {
            ResponseBuilder.notFound().build();
            throw new RuntimeException(e);
        }
    }

    private boolean isStaticPath(String requestURL) {
        return getStaticFolderNames().contains(requestURL.split("/")[1]);
    }
}
