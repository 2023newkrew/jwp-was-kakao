package webserver;

import controller.*;
import db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Database db;
    private final Map<String, Controller> controllerMap = new HashMap<>();

    public RequestHandler(Socket connectionSocket, Database db) {
        this.connection = connectionSocket;
        this.db = db;
        setControllerMap();
    }

    public void run() {
        logConnected();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            RequestHeader header = httpRequest.getRequestHeader();

            String uri = header.get("URI").orElseThrow(IllegalArgumentException::new);
            String uriWithOutParams = uri.split("\\?")[0];
            Controller controller = controllerMap.get(uriWithOutParams);

            // URI와 매핑된 컨트롤러를 찾을 수 없음
            if (controller == null) {
                controller = new MainController();
            }

            controller.process(httpRequest, new DataOutputStream(out));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void logConnected() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
    }

    private Map<String, Controller> setControllerMap() {
        controllerMap.put("/", new HelloController());
        controllerMap.put("/index.html", new ViewController("/index.html"));
        controllerMap.put("/login_failed.html", new ViewController("/user/login_failed.html"));
        controllerMap.put("/user/create", new UserCreateController(db));
        controllerMap.put("/user/login", new UserLoginController(db));
        return null;
    }
}
