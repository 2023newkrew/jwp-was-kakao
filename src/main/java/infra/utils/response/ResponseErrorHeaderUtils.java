package infra.utils.response;

import static infra.utils.response.ResponseUtilsConstants.HTTP_1_1_BASE;

import infra.exception.BaseException;
import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

public class ResponseErrorHeaderUtils {

    public static void makeErrorHeader(DataOutputStream dos, Logger logger, BaseException e) {
        try {
            dos.writeBytes(String.format(HTTP_1_1_BASE, HttpStatus.valueOf(e.getStatusCode())));
            dos.writeBytes("\r\n");
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }
}
