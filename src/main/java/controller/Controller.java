package controller;

import request.Request;
import response.Response;

public interface Controller {

    Response handleRequest(Request request);

}
