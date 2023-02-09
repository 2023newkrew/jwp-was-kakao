package controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.Controller;
import was.annotation.Mapping;
import was.annotation.RequestMethod;
import was.domain.response.Response;
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
            return Optional.of(Response.htmlBuilder()
                    .body(FileIoUtils.loadFileFromClasspath("./templates/index.html")).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Mapping(method = RequestMethod.GET, path = "/")
    public static Optional<Response> socket_out() {
        return Optional.of(Response.htmlBuilder()
                .body("Hello world".getBytes()).build());
    }
}
