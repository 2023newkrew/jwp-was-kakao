package utils;

import constant.HeaderConstant;
import model.enumeration.HttpStatus;
import model.response.HttpResponse;
import model.response.HttpResponse.HttpResponseBuilder;
import model.response.ResponseHeader;

import static constant.HeaderConstant.*;

public class ResponseBuilder {
    public static HttpResponseBuilder ok() {
        return HttpResponse.builder()
                .status(HttpStatus.OK);
    }

    public static HttpResponse found(String location) {
        ResponseHeader header = new ResponseHeader();
        header.put(LOCATION, location);

        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header(header)
                .build();
    }

    public static HttpResponse notFound() {
        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
