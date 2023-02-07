package webserver.testsupport;

import webserver.http.exceptionhandler.DefaultHttpExceptionHandlerMapping;
import webserver.http.requesthandler.DefaultHttpRequestHandlerMapping;

public class DefaultBaseE2ETest extends BaseE2ETest {
    public DefaultBaseE2ETest() {
        super(new DefaultHttpRequestHandlerMapping(), new DefaultHttpExceptionHandlerMapping());
    }
}
