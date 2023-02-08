package application;

import application.controller.JinHttpExceptionHandlerMapping;
import application.controller.JinHttpRequestHandlerMapping;
import webserver.testsupport.BaseE2ETest;

public class JinBaseE2ETest extends BaseE2ETest {
    public JinBaseE2ETest() {
        super(new JinHttpRequestHandlerMapping(), new JinHttpExceptionHandlerMapping());
    }
}
