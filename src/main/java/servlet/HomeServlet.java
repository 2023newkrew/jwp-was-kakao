package servlet;

import http.ContentType;
import http.HttpStatus;
import http.request.Request;
import http.response.Response;

import java.util.Objects;

public class HomeServlet implements Servlet {
    public static final String REQUEST_PATH = "/";
    private static HomeServlet instance;

    private HomeServlet() {}

    public static HomeServlet getInstance() {
        if (Objects.isNull(instance)) {
            instance = new HomeServlet();
        }
        return instance;
    }

    @Override
    public Response doGet(Request request) {
        byte[] body = "Hello world".getBytes();
        return Response.builder()
                .httpVersion(request.getVersion())
                .httpStatus(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .contentLength(body.length)
                .body(body)
                .build();
    }
}
