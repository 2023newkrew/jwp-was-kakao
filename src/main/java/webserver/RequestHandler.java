package webserver;

import controller.*;
import db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;
import view.ViewResolver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Database db;
    private final SessionManager sessionManager;
    Map<RequestInfo, Controller> controllerMap = new HashMap<>();
    private final ViewResolver viewResolver = new ViewResolver();

    public RequestHandler(Socket connectionSocket, Database db, SessionManager sessionManager) {
        this.connection = connectionSocket;
        this.db = db;
        this.sessionManager = sessionManager;
        setControllerMap();
    }

    public void run() {
        logConnected();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            HttpResponse httpResponse = new HttpResponse(out);

            handleRequest(httpRequest, httpResponse);

            httpResponse.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        RequestHeader header = httpRequest.getRequestHeader();

        // URI와 매핑된 컨트롤러 찾기
        String uri = header.get("URI").orElseThrow(IllegalArgumentException::new);
        HttpMethod method = HttpMethod.valueOf(header.get("method").orElseThrow(IllegalArgumentException::new));
        Controller controller = controllerMap.get(new RequestInfo(uri, method));

        // URI와 매핑된 컨트롤러를 찾을 수 없음. 정적 파일 연결 도와주는 컨트롤러를 할당.
        if (controller == null) {
            controller = new MainController(sessionManager);
        }

        ModelAndView modelAndView = controller.run(httpRequest, httpResponse);

        if (modelAndView == null) {
            return;
        }

        View view = viewResolver.resolve(modelAndView.getView());
        view.render(modelAndView.getModel(), httpResponse);
    }

    private void logConnected() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
    }

    private void setControllerMap() {
        controllerMap.put(new RequestInfo("/", HttpMethod.GET), new HelloController());
        controllerMap.put(new RequestInfo("/user/list", HttpMethod.GET), new UserListController(db, sessionManager));
        controllerMap.put(new RequestInfo("/user/create", HttpMethod.POST), new UserCreateController(db));
        controllerMap.put(new RequestInfo("/user/login", HttpMethod.POST), new UserLoginController(db, sessionManager));
    }
}
