package controller;

import webserver.*;

public class FileController extends BaseController{
    @Override
    void processRequestByMethod(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(Parser.getFileContent(request.getUri()));
            response.setHttpStatus(HttpStatus.OK);
        }
    }
}
