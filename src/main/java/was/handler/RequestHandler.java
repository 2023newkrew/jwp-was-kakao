package was.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.*;
import was.domain.Controllers;
import was.domain.PathPattern;
import was.domain.request.Request;
import was.domain.response.Response;
import was.domain.response.StatusCode;
import was.domain.response.Version;
import was.scanner.ClassAnnotationScanner;
import was.scanner.MethodAnnotationScanner;
import was.utils.RequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final Response RESPONSE_404 = Response.builder().version(Version.HTTP_1_1)
            .statusCode(StatusCode.NOT_FOUND).build();

    private Socket connection;
    private Map<PathPattern, Method> map;
    private Map<String, Method> staticMap;
    private Controllers controllers = new Controllers(ClassAnnotationScanner.getInstance().getClasses(Controller.class));

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initMap();
    }

    private void initMap() {
        map = MethodAnnotationScanner.getInstance().getMethods(Controller.class, Mapping.class).stream()
                .collect(Collectors.toMap(it -> PathPattern.from(it.getAnnotation(Mapping.class)), it -> it));
        staticMap = MethodAnnotationScanner.getInstance().getMethods(Controller.class, StaticMapping.class).stream()
                .collect(Collectors.toMap(it -> getFileType(it.getAnnotation(StaticMapping.class)), it -> it));
    }

    private String getFileType(StaticMapping staticMapping) {
        return staticMapping.fileType();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            Response response = createResponse(RequestUtils.getRequest(in)).orElse(RESPONSE_404);
            responseHeader(dos, response);
            responseBody(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Optional<Response> createResponse(Request request) {
        try {
            List<Method> methods = staticMap.keySet().stream()
                    .filter(it -> request.getPath().endsWith("." + it))
                    .map(it -> staticMap.get(it))
                    .collect(Collectors.toList());

            if (!methods.isEmpty()) {
                return (Optional<Response>) methods.get(0).invoke(controllers.getController(methods.get(0).getDeclaringClass()), request);
            }

            Method method = map.get(request.toPathPattern());
            if (method.isAnnotationPresent(QueryString.class) &&
                    (request.getParams() == null || request.getParams().isEmpty())) {
                return Optional.ofNullable(null);
            }
            if (method.isAnnotationPresent(RequestBody.class) &&
                    (request.getBody() == null || request.getBody().length() == 0)) {
                return Optional.ofNullable(null);
            }
            return (Optional<Response>) method.invoke(controllers.getController(method.getDeclaringClass()), request);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }

    private void responseHeader(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getHeader());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, Response response) {
        try {
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
