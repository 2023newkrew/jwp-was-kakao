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
public class CssController {
    private static final Logger logger = LoggerFactory.getLogger(CssController.class);

    @Mapping(method = RequestMethod.GET, path = "/css/styles.css")
    public static Optional<Response> css() {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/css")
                    .body(FileIoUtils.loadFileFromClasspath("./static/css/styles.css")).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Mapping(method = RequestMethod.GET, path = "/css/bootstrap.min.css")
    public static Optional<Response> bootstrapCss() {
        try {
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .contentType("text/css")
                    .body(FileIoUtils.loadFileFromClasspath("./static/css/bootstrap.min.css")).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }
}
