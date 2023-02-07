package servlet;

import exception.BadRequestException;
import exception.NotFoundException;
import http.ContentType;
import http.HttpMethod;
import http.HttpStatus;
import http.request.Request;
import http.response.Response;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ServletContainer {
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
                "/", HomeServlet.getInstance(),
                "/user/create", UserCreateServlet.getInstance()
        );
    }

    public Response dispatch(Request request) {
        try {
            return mapRequest(request);
        } catch (RuntimeException e) {
            return handleException(e);
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

    private Response handleException(RuntimeException e) {
        if (e instanceof BadRequestException) {
            return Response.builder().httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        if (e instanceof NotFoundException) {
            return Response.builder().httpStatus(HttpStatus.NOT_FOUND).build();
        }
        e.printStackTrace();
        throw e;
    }
}
