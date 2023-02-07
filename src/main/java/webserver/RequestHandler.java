package webserver;

import controller.ControllerSelector;
import dto.BaseResponseDto;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.request.Request;
import webserver.request.StartLine;
import webserver.request.StatusCode;
import webserver.response.Response;

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

            Request request = getRequest(bufferedReader);
            Response response = getResponse(request);

            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, response);
            responseBody(dos, response.getBody().getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {        // custom 처리
            throw new RuntimeException(e);
        }
    }

    private static Request getRequest(BufferedReader bufferedReader) throws IOException {
        // readLine 파싱
        String line = bufferedReader.readLine();
        StartLine startLine = RequestParser.extractStartLine(line);

        //  HTTP 헤더 파싱
        List<String> headerList = IOUtils.readUntilEmpty(bufferedReader);
        Map<String, String> headers = RequestParser.extractHeader(headerList);

        // HTTP body 파싱
        String rawBody = null;
        if (headers.containsKey("Content-Length")) {
            rawBody = IOUtils.readData(bufferedReader, Integer.parseInt(headers.get("Content-Length")));
        }
        Map<String, String> body = RequestParser.extractBodyOrQueryParam(rawBody);
        Request request = new Request(startLine, headers, body);
        return request;
    }

    private Response getResponse(Request request) throws IOException, URISyntaxException {
        String httpVersion = request.getStartLine().getHttpVersion();
        ResourceType resourceType = ResourceType.getResourceType(request);

        BaseResponseDto responseDto = null;
        // rest api
        if (resourceType == ResourceType.NONE) {
            responseDto = controllerSelector.runMethod(request);
        }
        // 파일 확장자
        if (resourceType == ResourceType.STATIC || resourceType == ResourceType.HTML) {
            String absolutePath = request.getStartLine().convertToAbsolutePath(resourceType);
            responseDto = new BaseResponseDto(StatusCode.OK,
                new String(FileIoUtils.loadFileFromClasspath(absolutePath)));
        }

        return responseDto.convertToResponse(httpVersion, request.getAcceptContentType());
    }

    private void responseHeader(DataOutputStream dos, Response response) {
        StatusCode statusCode = response.getStatusCode();
        try {
            dos.writeBytes(response.getVersion() + " " + statusCode.getCodeNum() + " " + statusCode.getCodeMessage() + " \r\n");
            for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                dos.writeBytes(header.getKey() + ": " + header.getValue() + " \r\n");
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
