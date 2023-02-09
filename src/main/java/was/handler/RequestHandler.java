package was.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.*;
import was.domain.PathPattern;
import was.domain.request.Request;
import was.domain.response.Response;
import was.domain.response.StatusCode;
import was.domain.response.Version;
import was.scanner.MethodAnnotationScanner;
import was.utils.RequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final Response RESPONSE_404 = Response.builder().version(Version.HTTP_1_1)
            .statusCode(StatusCode.NOT_FOUND).build();

    private final Socket connection;
    private Map<PathPattern, Method> map;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initMap();
    }

    private void initMap() {
        map = MethodAnnotationScanner.getInstance().getMethods(Controller.class, Mapping.class).stream()
                .collect(Collectors.toMap(it -> PathPattern.from(it.getAnnotation(Mapping.class)), it -> it));
    }

    private Optional<Response> mapping(Request request) {
        try {
            List<Object> args = new ArrayList<>();
            if(map.get(request.toPathPattern()).isAnnotationPresent(QueryString.class)){
                args.add(request.getParams());
            }
            if(map.get(request.toPathPattern()).isAnnotationPresent(RequestHeader.class)){
                args.add(request.getHeaders());
            }
            if(map.get(request.toPathPattern()).isAnnotationPresent(RequestBody.class)){
                args.add(request.getBody());
            }
            Object object = map.get(request.toPathPattern()).invoke(null, args.toArray());
            return (Optional<Response>) map.get(request.toPathPattern()).invoke(null, args.toArray());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void response(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getResponseLine());
            dos.writeBytes(response.getHeader());
            if(response.isValidBody()) {
                dos.writeBytes("\r\n\r\n");
                dos.write(response.getBody(), 0, response.getBody().length);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            Response response = mapping(RequestUtils.getRequest(in)).orElse(RESPONSE_404);
            response(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
