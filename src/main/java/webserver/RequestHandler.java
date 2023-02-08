package webserver;

import controller.ControllerSelector;
import dto.BaseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;

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
            while (s != null && !"".equals(s)) {
                requestStrings.add(s);
                s = bufferedReader.readLine();
            }
            RequestHeader requestHeader = RequestParser.parseHeader(requestStrings);

            String body = "";
            if (requestHeader.hasContentLength()) {
                body = IOUtils.readData(bufferedReader, Integer.parseInt(requestHeader.getHeaders().get("Content-Length")));
            }

            Request request = new Request(requestHeader, body);

            // response 생성
            sendResponse(out, request);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {        // custom 처리
            throw new RuntimeException(e);
        }
    }

    private void sendResponse(OutputStream out, Request request) throws IOException, URISyntaxException {
        DataOutputStream dos = new DataOutputStream(out);

        BaseResponseDto response = getResponse(request);
        responseHeader(dos, response);
        responseBody(dos, response.getBody().getBytes());
    }

    private BaseResponseDto getResponse(Request request) throws IOException, URISyntaxException {
        // 파일 확장자
        if (request.getHeader().getHttpMethod().equals(HttpMethod.GET)) {
            String requestExtension = request.getHeader().getExtension();
            ResourceType resourceType = ResourceType.getResourceType(requestExtension);
            if (resourceType != ResourceType.NONE) {
                request.getHeader().convertToAbsolutePath(resourceType);
                String contentType = request.getHeader().getContentType()
                        .orElse("text/" + requestExtension);

                return new BaseResponseDto(StatusCode.OK,
                        new String(FileIoUtils.loadFileFromClasspath(request.getHeader().getUrl())),
                        contentType);
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
            dos.writeBytes("Content-Type: " + response.getContentType() + ";charset=utf-8 \r\n");
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
