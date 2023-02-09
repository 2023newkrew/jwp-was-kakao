package servlet;

import exception.NotFoundException;
import http.request.Request;
import http.response.Response;

public interface Servlet {
    default Response handle(Request request) {
        if (request.getMethod().isGet()) {
            return doGet(request);
        }
        return doPost(request);
    }

    default Response doGet(Request request) {
        throw new NotFoundException();
    }

    default Response doPost(Request request) {
        throw new NotFoundException();
    }
}
