package webserver;

import controller.HomeController;
import controller.StaticFileController;
import controller.UserController;
import enums.ContentType;
import exceptions.InvalidQueryParameterException;
import exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import utils.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HomeController homeController;
    private final UserController userController;
    private final StaticFileController staticFileController;
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.homeController = HomeController.getInstance();
        this.userController = UserController.getInstance();
        this.staticFileController = StaticFileController.getInstance();
    }


    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = IOUtils.parseReqeust(in);
            HttpResponse response = handleHttpRequest(request);

            DataOutputStream dos = new DataOutputStream(out);
            response.writeToOutputStream(dos);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse handleHttpRequest(HttpRequest request) {
        try {
            return doHandleHttpRequest(request);
        } catch (ResourceNotFoundException e) {
            return HttpResponse.status(HttpStatus.NOT_FOUND).contentType(ContentType.JSON).body("Resource Not Found");
        } catch (URISyntaxException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).contentType(ContentType.JSON).body("Wrong URI Format");
        } catch (InvalidQueryParameterException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).contentType(ContentType.JSON).body("Invalid Query Parameter");
        } catch (IOException e) {
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON).body("Connection Error");
        } catch (Exception e) {
            return HttpResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(ContentType.JSON).body("Internal Server Error");
        }

    }

    private HttpResponse doHandleHttpRequest(HttpRequest request) throws IOException, URISyntaxException {
        String requestPath = request.getRequestPath();

        if (requestPath.startsWith("/user/create") && "GET".equals(request.getRequestMethod())) {
            return userController.createUserGet(request);
        }

        if (requestPath.startsWith("/user/create") && "POST".equals(request.getRequestMethod())) {
            return userController.createUserPost(request);
        }
        if (requestPath.equals("/")) {
            return homeController.rootPathGet(request);
        }

        if (FileIoUtils.isStaticFile(request)) {
            return staticFileController.staticFileGet(request);
        }
        throw new ResourceNotFoundException();
    }
}
