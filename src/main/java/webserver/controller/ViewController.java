package webserver.controller;

import model.HttpRequest;
import webserver.Controller;

public class ViewController implements Controller {

    @Override
    public String process(HttpRequest httpRequest) {
        return "./templates" + httpRequest.getUrl();
    }

    @Override
    public boolean isRedirectRequired() {
        return false;
    }
}
