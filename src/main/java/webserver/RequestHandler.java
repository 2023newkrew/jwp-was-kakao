package webserver;

import controller.ControllerSelector;
import dto.BaseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final ControllerSelector controllerSelector;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controllerSelector = new ControllerSelector();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // request 파싱
            List<String> requestStrings = new ArrayList<>();
            String s = bufferedReader.readLine();
            while (s != null) {
                requestStrings.add(s);
                s = bufferedReader.readLine();
            }
            Request request = RequestParser.parse(requestStrings);

            // response 생성
            BaseResponseDto response = getResponse(request);

            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, response);
            responseBody(dos, response.getBody().getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {        // custom 처리
            throw new RuntimeException(e);
        }
    }

    private BaseResponseDto getResponse(Request request) throws IOException, URISyntaxException {
        // 파일 확장자
        if (request.getHttpMethod().equals(HttpMethod.GET)) {
            ResourceType resourceType = ResourceType.getResourceType(request);
            if (resourceType != ResourceType.NONE) {
                request.convertToAbsolutePath(resourceType);
                return new BaseResponseDto(StatusCode.OK,
                        new String(FileIoUtils.loadFileFromClasspath(request.getUrl())));
            }
        }

        // path
        return controllerSelector.runMethod(request);
    }

    private void responseHeader(DataOutputStream dos, BaseResponseDto response) {
        StatusCode statusCode = response.getStatusCode();
        int bodyLength = response.getBody().getBytes().length;

        try {
            dos.writeBytes("HTTP/1.1 " + statusCode.getCodeNum() + " " + statusCode.getCodeMessage() + " \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + bodyLength + " \r\n");
            if (statusCode == StatusCode.FOUND) {
                dos.writeBytes("Location: /index.html");
            }
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
