package infra.utils.response;

import static infra.utils.response.ResponseUtilsConstants.HTTP_FOUND;
import static infra.utils.response.ResponseUtilsConstants.HTTP_OK;
import static infra.utils.response.ResponseUtilsConstants.LOCATION;
import static infra.utils.response.ResponseUtilsConstants.SET_COOKIE_JSESSIONID;

import infra.utils.request.MyCookie;
import java.io.DataOutputStream;
import java.io.IOException;
import org.slf4j.Logger;

public class Response302HeaderUtils {

    public static void make302ResponseWithCookie(DataOutputStream dos, String redirectUrl, MyCookie cookie,
                                                 Logger logger) {
        try {
            dos.writeBytes(HTTP_FOUND);
            dos.writeBytes(String.format(LOCATION, redirectUrl));
            makeCookie(dos, cookie, logger);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void make302ResponseHeader(DataOutputStream dos, String redirectUrl, Logger logger) {
        try {
            dos.writeBytes(HTTP_FOUND);
            dos.writeBytes(String.format(LOCATION, redirectUrl));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void makeCookie(DataOutputStream dos, MyCookie cookie, Logger logger) {
        try {
            dos.writeBytes(HTTP_OK);
            dos.writeBytes(String.format(SET_COOKIE_JSESSIONID, cookie.getValue()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
