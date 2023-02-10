package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import supports.HttpParser;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    public final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String httpRequest = IOUtils.getHttpRequest(br);

            HttpParser httpParser = new HttpParser(httpRequest);
            PathBinder pathBinder = new PathBinder();
            pathBinder.bind(out, br, httpParser);

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
