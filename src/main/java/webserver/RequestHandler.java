package webserver;

import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import model.dto.MyHeaders;
import model.dto.MyParams;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

import static db.DataBase.addUser;
import static utils.IOUtils.readData;
import static model.dto.ResponseHeaders.response302Header;
import static utils.UserFactory.createUser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private FrontController frontController;
    private MyHeaders headers;
    private MyParams params;

    public RequestHandler(Socket connection, FrontController frontController){
        this.frontController = frontController;
        this.connection = connection;
        this.headers = new MyHeaders();
        this.params = new MyParams();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line = br.readLine();

            boolean isFirstLine = false;

            // Request Header
            while(!(Objects.isNull(line) || line.equals(""))){
                isFirstLine = getFirstLineData(isFirstLine, line);

                getContentType(line);

                getContentLength(line);

                line = br.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);

            // [POST] Request Body (추후 다른 메서드로 확장 가능)
            if(headers.get("method").equals("POST")){
                getDataFromRequestBody(br, dos);
            }

            // Handler Mapping (FrontController는 Dispatcher Servlet의 역할을 수행)
            if(frontController.canHandle(headers, params)){
                frontController.handlerMapping(headers, params, dos);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // RequestBody에서 데이터 추출
    private void getDataFromRequestBody(BufferedReader br, DataOutputStream dos) {
        try{
            // Body 데이터 읽기
            String result = readData(br, Integer.parseInt(headers.get("contentLength")));

            // Key & Value로 Param값 추출
            Arrays.stream(result.split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                params.put(keyAndValue[0], keyAndValue[1]);
            });
        }
        catch(IOException e){
            logger.error(e.getMessage());
        }
    }

    // ContentLength 추출
    private void getContentLength(String line) {
        if(!line.startsWith("Content-Length: ")) return;

        String contentLength = line.split(" ")[1];
        headers.put("contentLength", contentLength);
    }

    // ContentType 추출
    private void getContentType(String line) {
        if(!line.startsWith("Accept: ")) return;

        var tokens = line.split(" ")[1];
        String contentType = null;

        if(tokens.length() == 1){
            contentType = tokens.split(";")[0];
        }
        if(tokens.length() > 1){
            contentType = tokens.split(",")[0];
        }

        headers.put("contentType", contentType);
    }

    // Request Header의 첫 줄에서 필요한 데이터(method, path) 추출
    private boolean getFirstLineData(boolean isFirstLine, String line){
        if(isFirstLine) return true;

        String[] tokens = line.split(" ");
        String method = tokens[0];
        String path = tokens[1];

        if(path.contains("?")){
            Arrays.stream(path.split("\\?")[1].split("&")).forEach(str -> {
                var keyAndValue = str.split("=");
                params.put(keyAndValue[0], keyAndValue[1]);
            });
            path = path.split("\\?")[0];
        }

        headers.put("method", method);
        headers.put("path", path);
        params.put("extension", getExtensionFromPath(path));

        return true;
    }

    private String getExtensionFromPath(String path){
        String[] tokens = path.split("\\.");
        if(tokens.length == 0) return "";
        return tokens[tokens.length - 1];
    }
}
