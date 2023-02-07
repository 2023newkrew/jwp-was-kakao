package webserver;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.MemorySessionRepository;
import repository.MemoryUserRepository;
import utils.FileIoUtils;
import webserver.controller.GlobalController;
import webserver.controller.RequestMappingHandler;
import webserver.request.Request;
import webserver.response.Response;
import webserver.service.SessionService;
import webserver.service.UserService;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URISyntaxException;

@RequiredArgsConstructor
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final RequestMappingHandler requestMappingHandler = new RequestMappingHandler(new GlobalController(new UserService(new MemoryUserRepository()), new SessionService(new MemorySessionRepository())));

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {
            Request request = Request.parse(reader);
            Response response = getResponseByPath(request);
            response.flush(new DataOutputStream(out));
        } catch (IOException | URISyntaxException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
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
