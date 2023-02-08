package infra.exception;

import infra.utils.Api;
import org.springframework.http.HttpStatus;

public class NotMatchException extends BaseException {
    public NotMatchException(String actual, String expected, String context, Api api) {
        super(makeMessage(actual,expected,context,api), HttpStatus.BAD_REQUEST.value());
    }

    private static String makeMessage(String actual, String expected, String context, Api api) {
        return String.format("\r\n"
                + "ACTUAL   : %s \r\n"
                + "EXPECTED : %s \r\n"
                + "CONTEXT  : %s \r\n"
                + "API      : %s \r\n", actual, expected, context, api);
    }


}
