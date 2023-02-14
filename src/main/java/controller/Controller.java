package controller;

import exception.RedirectException;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public interface Controller {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws RedirectException, IOException;
}
