package servlet;

import exception.BadRequestException;
import http.ContentType;
import http.HttpStatus;
import http.Uri;
import http.request.Request;
import http.request.RequestStartLine;
import http.response.Response;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceServlet implements Servlet {

    private static class ResourceServletHolder {
        private static final ResourceServlet instance = new ResourceServlet();
    }

    private ResourceServlet() {
    }

    public static ResourceServlet getInstance() {
        return ResourceServletHolder.instance;
    }

    @Override
    public Response doGet(Request request) {
        RequestStartLine startLine = request.getStartLine();
        ContentType contentType = ContentType.of(startLine.getUri().getExtension().orElseThrow(BadRequestException::new));
        try {
            byte[] body = getResource(startLine.getUri(), contentType);
            return Response.builder()
                    .httpVersion(startLine.getVersion())
                    .httpStatus(HttpStatus.OK)
                    .contentType(contentType)
                    .contentLength(body.length)
                    .body(body)
                    .build();
        } catch (IOException | URISyntaxException | NullPointerException e) {
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
