package webserver;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.controller.RequestMappingHandler;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.StatusCode;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final RequestMappingHandler requestMappingHandler;

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                OutputStream out = connection.getOutputStream()
        ) {
            Response response = processRequest(reader);
            response.flush(new DataOutputStream(out));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Response processRequest(BufferedReader reader) {
        Response response;
        try {
            Request request = Request.parse(reader);
            response = getResponseByPath(request);
        } catch (IllegalArgumentException | URISyntaxException | InvocationTargetException e) {
            response = Response.of(StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            response = Response.of(StatusCode.INTERNAL_SERVER_ERROR);
            logger.error(e.getMessage());
        }
        return response;
    }

    private Response getResponseByPath(Request request) throws IOException, URISyntaxException, InvocationTargetException, IllegalAccessException {
        String path = request.getPath();
        FileType fileType = request.getRequestFileType();

        if (fileType == FileType.HTML || fileType == FileType.ICO) {
            return Response.ok(FileIoUtils.loadFileFromClasspath("./templates" + path), fileType);
        }

        if (fileType == FileType.CSS || fileType == FileType.JS || fileType.isFont()) {
            return Response.ok(FileIoUtils.loadFileFromClasspath("./static" + path), fileType);
        }

        return requestMappingHandler.handle(request);
    }
}
