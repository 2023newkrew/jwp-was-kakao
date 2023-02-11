package was.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.Controller;
import was.annotation.StaticMapping;
import was.domain.request.Request;
import was.domain.response.Response;
import was.domain.response.ResponseHeader;
import was.domain.response.StatusCode;
import was.domain.response.Version;
import was.utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Controller
public class StaticController {
    private static final Logger logger = LoggerFactory.getLogger(StaticController.class);

    private StaticController() {
    }

    @StaticMapping(fileType = "html")
    public static Optional<Response> getHtml(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./templates/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("text/html;charset=utf-8")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "css")
    public static Optional<Response> getCss(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./static/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("text/css")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "png")
    public static Optional<Response> getPng(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./static/image/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("image/png")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "js")
    public static Optional<Response> getJs(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./static/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("text/javascript")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "ttf")
    public static Optional<Response> getTtf(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./static/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("font/ttf")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "woff")
    public static Optional<Response> getWoff(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./static/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("font/woff")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }

    @StaticMapping(fileType = "ico")
    public static Optional<Response> getIco(Request request) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./templates/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType("image/vnd.microsoft.icon")
                            .contentLength(body.length)
                            .build())
                    .body(body).build());
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return Optional.ofNullable(null);
    }
}
