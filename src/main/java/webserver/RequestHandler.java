package webserver;

import application.controller.HomeController;
import application.controller.StaticFileController;
import application.controller.UserController;
import webserver.enums.ContentType;
import webserver.enums.RequestMethod;
import webserver.exceptions.InvalidQueryParameterException;
import webserver.exceptions.InvalidRequestException;
import webserver.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.FileIoUtils;
import webserver.utils.IOUtils;

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
    private final Socket connection;

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
            HttpRequest request = IOUtils.parseRequest(in);
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
        } catch (InvalidRequestException e) {
            return HttpResponse.status(HttpStatus.BAD_REQUEST).contentType(ContentType.JSON).body("Invalid Request Format");
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
        String requestURL = request.getRequestURL();

        if (requestURL.startsWith("/user/create") && request.getRequestMethod() == RequestMethod.GET) {
            return userController.createUserGet(request);
        }
        if (requestURL.startsWith("/user/create") && request.getRequestMethod() == RequestMethod.POST) {
            return userController.createUserPost(request);
        }

        if (requestURL.equals("/") && request.getRequestMethod() == RequestMethod.GET) {
            return homeController.rootPathGet(request);
        }

        if (FileIoUtils.isStaticFile(request)) {
            return staticFileController.staticFileGet(request);
        }
        throw new ResourceNotFoundException();
    }
}
