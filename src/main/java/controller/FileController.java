package controller;

import common.HttpMethod;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;
import webserver.*;

public class FileController extends Controller {
    @Override
    void handleRequestByMethod(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(Parser.getFileContent(request.getUri()));
            response.setHttpStatus(HttpStatus.OK);
        }
    }
}
