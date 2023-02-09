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
public class CssController {
    private static final Logger logger = LoggerFactory.getLogger(CssController.class);

    @Mapping(method = RequestMethod.GET, path = "/css/styles.css")
    public static Optional<Response> css() {
        return getCssResponse("./static/css/styles.css");
    }

    @Mapping(method = RequestMethod.GET, path = "/css/bootstrap.min.css")
    public static Optional<Response> bootstrapCss() {
        return getCssResponse("./static/css/bootstrap.min.css");
    }

    private static Optional<Response> getCssResponse(String filePath) {
        try {
            return Optional.of(Response.cssBuilder()
                    .body(FileIoUtils.loadFileFromClasspath(filePath))
                    .build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }
}
