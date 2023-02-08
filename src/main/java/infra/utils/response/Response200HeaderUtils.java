package infra.utils.response;

import static infra.utils.response.ResponseUtilsConstants.CONTENT_LENGTH;
import static infra.utils.response.ResponseUtilsConstants.CONTENT_TYPE;
import static infra.utils.response.ResponseUtilsConstants.HTTP_OK;

import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.springframework.http.MediaType;

public class Response200HeaderUtils {

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, Logger logger) {
        try {
            dos.writeBytes(HTTP_OK);
            dos.writeBytes(String.format(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE));
            dos.writeBytes(String.format(CONTENT_LENGTH, lengthOfBodyContent));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent,
                                         Logger logger) {
        try {
            dos.writeBytes(HTTP_OK);
            dos.writeBytes(String.format(CONTENT_TYPE, contentType));
            dos.writeBytes(String.format(CONTENT_LENGTH, lengthOfBodyContent));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
