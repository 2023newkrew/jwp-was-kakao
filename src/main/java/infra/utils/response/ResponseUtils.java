package infra.utils.response;

import static infra.utils.response.Response200HeaderUtils.response200Header;
import static infra.utils.response.ResponseBodyUtils.responseBody;
import static infra.utils.response.ResponseUtilsConstants.STATIC;
import static infra.utils.response.ResponseUtilsConstants.TEMPLATES;

import infra.utils.FileIoUtils;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import org.slf4j.Logger;

public class ResponseUtils {

    public static void make200TemplatesResponse(String path, String contentType, DataOutputStream dataOutputStream,
                                                Logger logger) {
        make200Response(path, contentType, dataOutputStream, logger, TEMPLATES);
    }


    public static void make200StaticResponse(String path, String contentType, DataOutputStream dataOutputStream,
                                             Logger logger) {
        make200Response(path, contentType, dataOutputStream, logger, STATIC);
    }

    public static void make200Response(String path, String contentType, DataOutputStream dataOutputStream,
                                       Logger logger, String classPath) {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(classPath + path);
            response200Header(dataOutputStream, contentType, body.length, logger);
            responseBody(dataOutputStream, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void make200Response(byte[] body, String contentType, DataOutputStream dataOutputStream,
                                       Logger logger) {
        response200Header(dataOutputStream, contentType, body.length, logger);
        responseBody(dataOutputStream, body);
    }



}
