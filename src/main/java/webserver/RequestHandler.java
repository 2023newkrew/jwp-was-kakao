package webserver;

import static utils.FileIoUtils.loadFileFromClasspath;

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
            HttpRequest httpRequest = HttpRequest.parse(bufferedReader);

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
        String requestPath = request.getPath();
        FilenameExtension extension = request.getFilenameExtension();
        if (extension.isExistStaticFolder()) {
            return HttpResponse.ok(loadFileFromClasspath("./static" + requestPath), extension);
        }
        if (extension.isExistTemplateFolder()) {
            return HttpResponse.ok(loadFileFromClasspath("./templates" + requestPath), extension);
        }
        return HandlerMapping.handle(request);
    }

}
