package infra;

import infra.http.HttpMessageBase;
import infra.http.request.HttpRequest;
import infra.http.response.HttpResponse;
import infra.http.response.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private UriMap uriMap;

    public RequestHandler(Socket connectionSocket, UriMap uriMap) {
        this.connection = connectionSocket;
        this.uriMap = uriMap;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = RequestParser.parse(br);
            if (!request.getVersion().equals(HttpMessageBase.DEFAULT_VERSION)) {
                this.sendResponse(dos, new HttpResponse(HttpResponseStatus.BAD_REQUEST));
            }

            Controller controller = this.uriMap.controller(request.getUri());
            if (controller == null) {
                this.sendResponse(dos, new HttpResponse(HttpResponseStatus.BAD_REQUEST));
            }
            this.sendResponse(dos, controller.response(request));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, HttpResponse response) throws IOException {
        byte[] output = response.flat();
        dos.write(output, 0, output.length);
        dos.flush();
    }
//
//    private void handlePost(Headers headers, BufferedReader br, DataOutputStream dos) throws IOException {
//        String url = headers.getUrl();
//        if (url.startsWith("/user/create")) {
//            responseRegister(IOUtils.readData(br, headers.getContentLength()), dos);
//            return;
//        }
//    }
//
//    private void responseRegister(String params, DataOutputStream dos) {
//        RequestParameters requestParameters = parse(params);
//        DataBase.addUser(new User(
//                requestParameters.get("userId"),
//                requestParameters.get("password"),
//                requestParameters.get("name"),
//                requestParameters.get("email"))
//        );
//        response302Header(dos, "/index.html");
//    }
//
//    private void response302Header(DataOutputStream dos, String location) {
//        try {
//            dos.writeBytes("HTTP/1.1 302 Found \r\n");
//            dos.writeBytes("Location: " + location);
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    public static RequestParameters parse(String query) {
//        RequestParameters requestParameters = new RequestParameters();
//        String[] params = query.split("&");
//        for (String param : params) {
//            String[] splitParam = param.split("=");
//            if (splitParam.length != 2) {
//                return null;
//            }
//            requestParameters.put(splitParam[0], splitParam[1]);
//        }
//        return requestParameters;
//    }
}
