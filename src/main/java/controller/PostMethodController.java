package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface PostMethodController {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException;
}
