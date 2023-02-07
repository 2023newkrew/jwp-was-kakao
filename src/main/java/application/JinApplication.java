package application;

import application.controller.JinHttpExceptionHandlerMapping;
import application.controller.JinHttpRequestHandlerMapping;
import webserver.WebServer;

public class JinApplication {
    public static void main(String[] args) throws Exception {
        WebServer.run(args, new JinHttpRequestHandlerMapping(), new JinHttpExceptionHandlerMapping());
    }
}
