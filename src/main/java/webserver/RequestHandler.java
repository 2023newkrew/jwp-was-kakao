package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import webserver.controller.ControllerMethod;
import webserver.controller.RequestController;
import webserver.request.Request;
import webserver.response.Response;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = new Request(br);
            Response response = new Response(request);

            mapRequest(request, response);
            sendResponse(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void mapRequest(Request request, Response response) {
        ControllerMethod method = RequestController.getMappedMethod(request);
        if (method != null) {
            method.handle(request, response);
            return;
        }
        // resource 응답
        String rootPath = "./templates";
        if (hasStaticPath(request)){
            rootPath = "./static";
        }
        setResource(rootPath + request.getPath(), response);
    }

    private void setResource(String path, Response response) {
        try {
            response.setBody(FileIoUtils.loadFileFromClasspath(path));
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setBody("404 Not Found - 요청한 페이지를 찾을 수 없습니다.".getBytes());
            response.setStatus(HttpStatus.NOT_FOUND);
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getHeader());
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasStaticPath(Request request) {
        String path = request.getPath();
        if (path == null) return false;
        String[] pathTokens = path.split("/");
        if (pathTokens.length < 2) return false;
        return StaticDirectory.resolve(pathTokens[1].toUpperCase()) != null;
    }
}

