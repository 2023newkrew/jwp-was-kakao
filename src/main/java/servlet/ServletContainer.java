package servlet;

import exception.BadRequestException;
import exception.NotFoundException;
import http.ContentType;
import http.HttpMethod;
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
    private static ServletContainer instance;
    private final Map<String, Servlet> servlets;

    private ServletContainer() {
        servlets = initiateContainer();
    }

    public static ServletContainer getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ServletContainer();
        }
        return instance;
    }

    private Map<String, Servlet> initiateContainer() {
        return Map.of(
                HomeServlet.REQUEST_PATH, HomeServlet.getInstance(),
                UserCreateServlet.REQUEST_PATH, UserCreateServlet.getInstance()
        );
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
            return ResourceServlet.getInstance().doGet(request);
        }
        Servlet servlet = servlets.getOrDefault(request.getUri().getPath(), null);
        if (Objects.isNull(servlet)) {
            throw new NotFoundException();
        }
        if (request.getMethod().equals(HttpMethod.GET)) {
            return servlet.doGet(request);
        }
        return servlet.doPost(request);
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
