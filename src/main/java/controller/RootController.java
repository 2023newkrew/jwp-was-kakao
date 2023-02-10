package controller;

import exception.RedirectException;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class RootController implements Controller {
    @Override
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException {
        httpResponse.addHeader("Content-Type", "text/plain;charset=utf-8");
        httpResponse.changeBody("Hello world".getBytes());
    }
}
