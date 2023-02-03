package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

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
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            HeaderInfo headerInfo = getHeaderInfo(bufferedReader);

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // TODO path 를 확인하고 처리해주
            if (headerInfo.getMethod().equals(HttpMethod.POST) && headerInfo.getPath().equals("/user/create")) {
                User user = new User(
                        headerInfo.getBodyValue("userId"),
                        headerInfo.getBodyValue("password"),
                        headerInfo.getBodyValue("name"),
                        headerInfo.getBodyValue("email")
                );
                DataBase.addUser(user);
                return;
            }

            byte[] body = headerInfo.getResponse();
            response200Header(dos, body.length, headerInfo.getAccept());
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HeaderInfo getHeaderInfo(BufferedReader bufferedReader) throws IOException {
        HeaderInfo headerInfo = new HeaderInfo(bufferedReader.readLine());
        String line = bufferedReader.readLine();
        while (!"".equals(line) && Objects.nonNull(line)) {
            System.out.println(line);
            headerInfo.readNextLine(line);
            line = bufferedReader.readLine();
        }
        String bodyLengthString = headerInfo.getHeaderValue("Content-Length");
        if (Objects.nonNull(bodyLengthString)) {
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(bodyLengthString));
            headerInfo.setBodyParams(body);
        }
        return headerInfo;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8 \r\n");
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

