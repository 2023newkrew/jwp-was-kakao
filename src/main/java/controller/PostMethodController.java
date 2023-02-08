package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public interface PostMethodController {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
