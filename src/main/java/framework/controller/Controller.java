package framework.controller;

import framework.request.Request;
import framework.response.Response;

public interface Controller {

    Response handleRequest(Request request);

    boolean canHandle(String uri);
}
