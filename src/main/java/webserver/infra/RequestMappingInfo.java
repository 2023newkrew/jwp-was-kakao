package webserver.infra;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import model.HttpRequest;
import model.annotation.Api;
import model.enumeration.ContentType;
import model.enumeration.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMappingInfo {
    private Object controller;
    private HttpMethod httpMethod;
    private String URL;
    private ContentType consumes;

    public static RequestMappingInfo from(Method method) {
        Api annotation = method.getDeclaredAnnotation(Api.class);
        return new RequestMappingInfo(
                method.getDeclaringClass(),
                annotation.method(),
                annotation.url(),
                annotation.consumes()
        );
    }
}

