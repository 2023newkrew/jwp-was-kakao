package controller;

import exception.BadRequestException;
import http.ContentType;
import http.HttpStatus;
import http.Uri;
import http.request.Request;
import http.response.Response;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class ResourceServlet implements Servlet {

    private static ResourceServlet instance;

    private ResourceServlet() {
    }

    public static ResourceServlet getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ResourceServlet();
        }
        return instance;
    }

    @Override
    public Response doGet(Request request) {
        ContentType contentType = ContentType.of(request.getUri().getExtension().orElseThrow(BadRequestException::new));
        try {
            byte[] body = getResource(request.getUri(), contentType);
            return Response.builder()
                    .httpStatus(HttpStatus.OK)
                    .contentType(contentType)
                    .contentLength(body.length)
                    .body(body)
                    .build();
        } catch (IOException | URISyntaxException e) {
            throw new BadRequestException();
        }
    }

    private byte[] getResource(Uri uri, ContentType contentType) throws IOException, URISyntaxException {
        if (contentType.equals(ContentType.HTML)) {
            return FileIoUtils.loadFileFromClasspath("templates" + uri.getUri());
        }
        return FileIoUtils.loadFileFromClasspath("static" + uri.getUri());
    }
}
