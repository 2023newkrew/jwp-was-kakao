package servlet;

import exception.AuthorizedException;
import exception.BadRequestException;
import exception.NotFoundException;
import exception.UnauthorizedException;
import filter.FilterManager;
import http.ContentType;
import http.HttpStatus;
import http.request.Request;
import http.response.Response;
import http.response.ResponseBuilder;
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

    private static final FilterManager filterManager = FilterManager.getInstance();

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
            filterManager.mapFilter(request);
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
        Servlet servlet = servlets.getOrDefault(request.getStartLine().getUri().getPath(), null);
        if (Objects.isNull(servlet)) {
            throw new NotFoundException();
        }
        return servlet;
    }

    private boolean isStaticResourceRequest(Request request) {
        Optional<String> extension = request.getStartLine().getUri().getExtension();
        return extension.isPresent() && ContentType.isFileExtension(extension.get());
    }

    private Response handleException(Request request, RuntimeException e) {
        ResponseBuilder commonBuilder = Response.builder()
                .httpVersion(request.getStartLine().getVersion());

        if (e instanceof BadRequestException) {
            return commonBuilder
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (e instanceof NotFoundException) {
            return commonBuilder
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        if (e instanceof UnauthorizedException) {
            return commonBuilder
                    .httpStatus(HttpStatus.FOUND)
                    .location("/user/login.html")
                    .build();
        }
        if (e instanceof AuthorizedException) {
            return commonBuilder
                    .httpStatus(HttpStatus.FOUND)
                    .location("/index.html")
                    .build();
        }
        logger.error("InternalServerError : {}", e.getMessage());
        return commonBuilder
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
