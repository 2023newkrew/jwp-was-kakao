package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.ModelAndView;

public interface Controller {
    ModelAndView run(HttpRequest request, HttpResponse response);
}
