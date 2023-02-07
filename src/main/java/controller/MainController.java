package controller;

import annotation.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Controller
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Mapping(method = RequestMethod.GET, path = "/index.html")
    public static Optional<Response> index() {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/html;charset=utf-8")
                    .body(FileIoUtils.loadFileFromClasspath("./templates/index.html")).build());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(null);
    }

    @Mapping(method = RequestMethod.GET, path = "/css/styles.css")
    public static Optional<Response> css() {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/css")
                    .body(FileIoUtils.loadFileFromClasspath("./static/css/styles.css")).build());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(null);
    }

    @Mapping(method = RequestMethod.GET, path = "/css/bootstrap.min.css")
    public static Optional<Response> bootstrapCss() {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/css")
                    .body(FileIoUtils.loadFileFromClasspath("./static/css/bootstrap.min.css")).build());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(null);
    }

    @Mapping(method = RequestMethod.GET, path = "/")
    public static Optional<Response> socket_out() {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .contentType("text/html;charset=utf-8")
                .body("Hello world".getBytes()).build());
    }

    @RequestBody
    @Mapping(method = RequestMethod.POST, path="/user/create")
    public static Optional<Response> createUser(String body){
        UserService.createUser(body);
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.FOUND)
                .location("/index.html")
                .build());
    }
}
