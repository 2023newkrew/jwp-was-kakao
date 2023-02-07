package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import webserver.handler.HandlerMapping;
import webserver.request.Request;
import webserver.response.Response;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {
            Request request = Request.parse(reader);
            Response response = findResponseByPath(request);
            response.flush(new DataOutputStream(out));
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private Response findResponseByPath(Request request) throws IOException, URISyntaxException {
        String path = request.getPath();
        FileType fileType = request.findRequestedFileType();

        if (fileType.isTemplatePath()) {
            return Response.ok(FileIoUtils.loadFileFromClasspath("./templates" + path), fileType);
        }

        if (fileType.isStaticPath()) {
            return Response.ok(FileIoUtils.loadFileFromClasspath("./static" + path), fileType);
        }

        return HandlerMapping.handle(request);
    }
}
