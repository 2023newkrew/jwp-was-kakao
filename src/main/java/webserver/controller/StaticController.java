package webserver.controller;

import model.HttpRequest;
import webserver.Controller;

public class StaticController implements Controller {

    @Override
    public String process(HttpRequest httpRequest) {
        return "./static" + httpRequest.getUrl();
    }

    @Override
    public boolean isRedirectRequired() {
        return false;
    }
}
