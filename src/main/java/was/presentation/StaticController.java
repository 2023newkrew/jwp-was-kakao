package was.presentation;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.annotation.Controller;
import was.annotation.StaticMapping;
import was.domain.StaticType;
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
@NoArgsConstructor
public class StaticController {
    private static final Logger logger = LoggerFactory.getLogger(StaticController.class);

    @StaticMapping()
    public Optional<Response> getStatic(Request request, StaticType type) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath("./" + type.getBasePath() + "/" + request.getPath());
            return Optional.of(Response.builder()
                    .version(Version.HTTP_1_1)
                    .statusCode(StatusCode.OK)
                    .responseHeader(ResponseHeader.builder()
                            .contentType(type.getContentType())
                            .contentLength(body.length)
                            .build())
                    .body(body)
                    .build());

        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return Optional.ofNullable(null);
    }
}
