package utils;

import model.enumeration.HttpStatus;
import model.response.HttpResponse;
import model.response.HttpResponse.HttpResponseBuilder;

public class ResponseBuilder {
    public static HttpResponseBuilder ok() {
        return HttpResponse.builder()
                .status(HttpStatus.OK);
    }

    public static HttpResponseBuilder found() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND);
    }

    public static HttpResponseBuilder notFound() {
        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND);
    }
}
