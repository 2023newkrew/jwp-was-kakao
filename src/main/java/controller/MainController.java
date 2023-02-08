package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestMethod;
import was.domain.response.Response;
import was.domain.response.StatusCode;
import was.domain.response.Version;
import was.utils.FileIoUtils;

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

    @Mapping(method = RequestMethod.GET, path = "/")
    public static Optional<Response> socket_out() {
        return Optional.of(Response.builder()
                .version(Version.HTTP_1_1)
                .statusCode(StatusCode.OK)
                .contentType("text/html;charset=utf-8")
                .body("Hello world".getBytes()).build());
    }
}
