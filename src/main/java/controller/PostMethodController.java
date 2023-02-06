package controller;

import webserver.HttpRequest;

public interface PostMethodController {
    String process(HttpRequest httpRequest);
}
