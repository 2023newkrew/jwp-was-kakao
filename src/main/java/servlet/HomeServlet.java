package servlet;

import http.ContentType;
import http.HttpStatus;
import http.request.Request;
import http.response.Response;

@ServletMapping(uri = "/")
public class HomeServlet implements Servlet {
    private static class HomeServletHolder {
        private static final HomeServlet instance = new HomeServlet();
    }

    private HomeServlet() {}

    public static HomeServlet getInstance() {
        return HomeServletHolder.instance;
    }

    @Override
    public Response doGet(Request request) {
        byte[] body = "Hello world".getBytes();
        return Response.builder()
                .httpVersion(request.getStartLine().getVersion())
                .httpStatus(HttpStatus.OK)
                .contentType(ContentType.HTML)
                .contentLength(body.length)
                .body(body)
                .build();
    }
}
