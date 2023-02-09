package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException;
}
