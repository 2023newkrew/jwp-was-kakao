package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.FileIoUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ResourceResolver resourceResolver;
    private final HandlerMapping handlerMapping;

    public RequestHandler (Socket connection){
        this(connection, new ResourceResolver(), new HandlerMapping());
    }

    public RequestHandler(Socket connection, ResourceResolver resourceResolver, HandlerMapping handlerMapping){
        this.request = new ServletRequest(connection);
        this.response = new ServletResponse(connection);
        this.resourceResolver = resourceResolver;
        this.handlerMapping = handlerMapping;
    }

    public void run() {
        String url = request.getRequestURI();
        System.out.println("url = " + url);

        if(resourceResolver.isResolvable(request.getRequestURI())){
            String resourceLocation = resourceResolver.resolve(url);
            String resourceUri = resourceLocation + url;

            try {
                byte[] data = FileIoUtils.loadFileFromClasspath(resourceUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
//            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello world".getBytes();
//            response200Header(dos, body.length);
//            responseBody(dos, body);
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
