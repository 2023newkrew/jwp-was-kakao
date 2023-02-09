package servlet;

import exception.BadRequestException;
import exception.NotFoundException;
import http.ContentType;
import http.HttpStatus;
import http.request.Request;
import http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ServletContainer {
    private static final Logger logger = LoggerFactory.getLogger(ServletContainer.class);

    private static class ServletContainerHolder {
        private static final ServletContainer instance = new ServletContainer();
    }

    private static final ResourceServlet resourceServlet = ResourceServlet.getInstance();

    private static final Map<String, Servlet> servlets;

    private ServletContainer() {

    }

    static {
        servlets = ServletLoader.load();
    }

    public static ServletContainer getInstance() {
        return ServletContainerHolder.instance;
    }

    public Response serve(Request request) {
        try {
            return mapRequest(request);
        } catch (RuntimeException e) {
            return handleException(request, e);
        }
    }

    private Response mapRequest(Request request) {
        if (isStaticResourceRequest(request)) {
            return resourceServlet.handle(request);
        }
        Servlet servlet = getServlet(request);
        return servlet.handle(request);
    }

    private Servlet getServlet(Request request) {
        Servlet servlet = servlets.getOrDefault(request.getUri().getPath(), null);
        if (Objects.isNull(servlet)) {
            throw new NotFoundException();
        }
        return servlet;
    }

    private boolean isStaticResourceRequest(Request request) {
        Optional<String> extension = request.getUri().getExtension();
        return extension.isPresent() && ContentType.isFileExtension(extension.get());
    }

    private Response handleException(Request request, RuntimeException e) {
        if (e instanceof BadRequestException) {
            return Response.builder().httpVersion(request.getVersion()).httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        if (e instanceof NotFoundException) {
            return Response.builder().httpVersion(request.getVersion()).httpStatus(HttpStatus.NOT_FOUND).build();
        }
        logger.error("InternalServerError : {}", e.getMessage());
        return Response.builder().httpVersion(request.getVersion()).httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
