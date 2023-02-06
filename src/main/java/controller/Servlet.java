package controller;

import exception.NotFoundException;
import http.request.Request;
import http.response.Response;

public interface Servlet {
    default Response doGet(Request request) {
        throw new NotFoundException();
    }

    default Response doPost(Request request) {
        throw new NotFoundException();
    }
}
