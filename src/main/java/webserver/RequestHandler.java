package webserver;

import static utils.FileIoUtils.loadFileFromRequestTarget;
import static utils.IOUtils.parseHttpRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import webserver.handler.HandlerMapping;
import webserver.request.HttpRequest;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            HttpRequest httpRequest = parseHttpRequest(bufferedReader);

            DataOutputStream dos = new DataOutputStream(out);

            HttpResponse response = execute(httpRequest);
            response.writeResponse(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private HttpResponse execute(HttpRequest request) throws IOException, URISyntaxException{
        String requestTarget = request.getPath();

        if (isFileRequestTarget(requestTarget)) {
            byte[] body = loadFileFromRequestTarget(requestTarget);
            String[] splitTarget = requestTarget.split("\\.");
            FilenameExtension extension = FilenameExtension.from(splitTarget[splitTarget.length - 1]);
            return HttpResponse.ok(body, extension);
        }
        return HandlerMapping.handle(request);
    }

    private static boolean isFileRequestTarget(String requestTarget) {
        return requestTarget.contains(".");
    }

    private static boolean isNullOrEmpty(String line) {
        return line == null || "".equals(line);
    }

}
