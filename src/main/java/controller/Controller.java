package controller;

import common.HttpRequest;
import common.HttpResponse;

public interface Controller {
    void process(HttpRequest request, HttpResponse response);
}
