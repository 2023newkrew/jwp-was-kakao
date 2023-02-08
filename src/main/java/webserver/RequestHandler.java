package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.domain.*;
import webserver.utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RoutingHandler routingHandler;

    public RequestHandler(Socket connectionSocket, RoutingHandler routingHandler) {
        this.connection = connectionSocket;
        this.routingHandler = routingHandler;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            HttpRequestReader requestReader = new HttpRequestReader(httpRequest);

            byte[] body = "Hello world".getBytes();

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = HttpResponse.builder(HttpStatus.OK)
                    .contentLength(body.length)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(body)
                    .build();

            if (requestReader.isFile()) {
                FileExtensions requestedFileExtension = FileExtensions.of(requestReader.getExtension());
                try {
                    body = FileIoUtils.loadFileFromClasspath(requestReader.getPath());
                    response = HttpResponse.builder(HttpStatus.OK)
                            .contentType(requestedFileExtension.getMediaType().value())
                            .contentLength(body.length)
                            .body(body)
                            .build();
                } catch (IOException e) {
                    logger.error(e.getMessage() + "\n" +
                            Arrays.toString(e.getStackTrace())
                    );
                }
            } else if (routingHandler.canHandle(httpRequest.getMethod(), httpRequest.getPath())) {
                Context context = new Context(requestReader);
                routingHandler.getHandler(httpRequest.getMethod(), httpRequest.getPath()).accept(context);
                response = context.getHttpResponse();
            }

            logger.info("{} {} - {}:{}", httpRequest.getMethod(), httpRequest.getPath(), connection.getInetAddress(), connection.getPort());
            HttpResponseWriter writer = new HttpResponseWriter(response);
            dos.write(writer.writeAsByte());

        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage() + "\n" +
                    Arrays.toString(e.getStackTrace())
            );
        }
    }
}
